package com.ecom.inventory.configurations;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Support for Java 8 Date/Time types
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Configure ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule for LocalDate, LocalDateTime support
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL // Enable default typing for non-final classes
        );

        // Serializer using ObjectMapper
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Default Redis Cache Configuration
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // Default TTL for all caches
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                );

        // Custom OTP Cache Configuration (e.g., 5-minute TTL for OTP)
        RedisCacheConfiguration otpCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(1))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                );

        // Define multiple cache configurations
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("otpCache", otpCacheConfig); // Custom TTL for OTP Cache
        cacheConfigurations.put("defaultCache", defaultCacheConfig); // Generic Default Cache

        // Build RedisCacheManager with specific caches
        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(cacheConfigurations)  // Custom caches
                .cacheDefaults(defaultCacheConfig)                    // Fallback cache
                .build();
    }

    /**
     * Configure RedisConnectionFactory to work with a Redis Cluster.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Define Redis Cluster nodes
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(
                Arrays.asList(
                        "127.0.0.1:7001",
                        "127.0.0.1:7002",
                        "127.0.0.1:7003",
                        "127.0.0.1:7004",
                        "127.0.0.1:7005",
                        "127.0.0.1:7006"
                )
        );
        // Create LettuceConnectionFactory for Redis
        return new LettuceConnectionFactory(clusterConfiguration);
    }
}
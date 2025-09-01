package com.ecom.inventory.services;

import com.ecom.inventory.dtos.ProductRequestDto;
import com.ecom.inventory.dtos.ProductResponseDto;
import com.ecom.inventory.entity.Product;
import com.ecom.inventory.exceptions.AlreadyExistsException;
import com.ecom.inventory.exceptions.NotFoundException;
import com.ecom.inventory.repositories.ProductRepository;
import com.ecom.inventory.services.interfaces.ProductServiceInteface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductServiceInteface {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    private ProductResponseDto mapToDto(Product product) {
        ProductResponseDto dto = objectMapper.convertValue(product, ProductResponseDto.class);

        // compute effective price
        double effectivePrice = product.getCost();
        if (product.getDiscount() != null && product.getDiscount() > 0) {
            effectivePrice = effectivePrice - (effectivePrice * (product.getDiscount() / 100));
        }
        dto.setEffectivePrice(effectivePrice);

        return dto;
    }

    @Override
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        productRepository.findByNameAndSupplier(productRequestDto.getName() , productRequestDto.getSupplier())
                .ifPresent(x->{
                    throw new AlreadyExistsException("Product already uploaded" +productRequestDto.getName());
                });
        Product product = objectMapper.convertValue(productRequestDto, Product.class);
        Product saved = productRepository.save(product);
        return mapToDto(saved);
    }

    @Override
    @CacheEvict(value = "productId", key = "'product_' + #productId")
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto updatedProduct) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));

        existing.setName(updatedProduct.getName());
        existing.setCost(updatedProduct.getCost());
        existing.setSupplier(updatedProduct.getSupplier());
        existing.setSupplierLocation(updatedProduct.getSupplierLocation());
        existing.setSpecs(updatedProduct.getSpecs());
        existing.setCount(updatedProduct.getCount());
        existing.setDiscount(updatedProduct.getDiscount());

        return mapToDto(productRepository.save(existing));
    }

    @Override
    @CacheEvict(value = "productId", key = "'product_' + #productId")
    public void deleteProduct(Long productId) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));
        productRepository.delete(existing);
    }

    @Override
    public ProductResponseDto applyDiscount(Long productId, Double discount) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));
        product.setDiscount(discount);
        return mapToDto(productRepository.save(product));
    }

    @Override
    @Cacheable(value = "products", key = "'page_' + #page + '_size_' + #size")
    public List<ProductResponseDto> getAllProducts(int page , int size) {
        Pageable pageable = PageRequest.of(page , size);
        return productRepository.findAll(pageable)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value="productId" , key="'product_'+ #productId")
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));
        return mapToDto(product);
    }
}
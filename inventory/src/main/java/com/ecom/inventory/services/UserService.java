package com.ecom.inventory.services;

import com.ecom.inventory.dtos.requestdto.PasswordDto;
import com.ecom.inventory.dtos.requestdto.SignUpRequestDto;
import com.ecom.inventory.dtos.requestdto.UserProfileUpdateDto;
import com.ecom.inventory.entity.User;
import com.ecom.inventory.exceptions.InvalidOtpException;
import com.ecom.inventory.exceptions.NotFoundException;
import com.ecom.inventory.exceptions.AlreadyExistsException;
import com.ecom.inventory.repositories.UserRepository;
import com.ecom.inventory.services.interfaces.UserServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private CacheManager cacheManager;
    private JavaMailSender mailSender;
    private ObjectMapper objectMapper;

    private static final int OTP_EXPIRATION_MINUTES = 10;


    @Override
    public User signup(SignUpRequestDto user) {
        boolean isVerified = verifyOTP(user.getEmail(), user.getOtp());
        User savedUser = objectMapper.convertValue(user , User.class);
        if(isVerified) {
            return userRepository.save(savedUser);
        }
        else{
            throw new InvalidOtpException("The otp is invalid or expired, Pls try again");
        }
    }

    public String generateOtp(String useremail) {
        // Remove leading/trailing whitespace
        String email = useremail.trim();

        System.out.println("Sanitized email: '" + email + "'");

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new AlreadyExistsException("User already exists with email: " + email);
        });


        System.out.println("otp going to generate");
        String otp = generateOTP(email);
        System.out.println("sending email");
        sendOTPEmail(email, otp);
        return otp;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }


    public User signin(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new NotFoundException("Invalid credentials for email: " + email));
    }


    public User updateAddress(Long userId, String newAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        user.setAddress(newAddress);
        return userRepository.save(user);
    }

    public User updateProfile(Long userId, UserProfileUpdateDto updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        user.setName(updatedUser.getName());
        user.setAddress(updatedUser.getAddress());

        return userRepository.save(user);
    }

    @Override
    public User getProfile(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with this Id doesn't exist"));

    }

    @Override
    public User updatePassword(String email, PasswordDto password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        user.setPassword(password.getPassword());
        return userRepository.save(user);
    }

    private String generateOTP(String email) {
        String otp = RandomStringUtils.randomNumeric(6);

        Cache cache = cacheManager.getCache("otpCache");
        cache.put(email, otp);
        System.out.println("generating otp and sending it now ");
        sendOTPEmail(email, otp);
        return otp;
    }

    private void sendOTPEmail(String email, String otp) {
        try {
            // Debugging: Log the email before sending
            System.out.println("Attempting to send email to: '" + email + "'");

            // Extra validation step
            if (!isValidEmail(email)) {
                throw new IllegalArgumentException("Invalid email format: " + email);
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            System.out.println("mail is set now");

            helper.setTo(email);
            System.out.println("1 - Set recipient to: " + email);

            helper.setSubject("Your OTP Code for Ecom");
            helper.setText("Your OTP code is: " + otp + "\n\nThis OTP is valid for " + OTP_EXPIRATION_MINUTES + " minutes.");

            System.out.println("mail sending now");
            mailSender.send(message);

            System.out.println("Mail sent successfully to: " + email);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Invalid email provided: " + email);
            throw e; // Rethrow for tracking
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    private boolean verifyOTP(String email, String otp) {
        Cache cache = cacheManager.getCache("otpCache");
        if (cache != null) {
            String cachedOtp = cache.get(email, String.class);
            if (cachedOtp != null && cachedOtp.equals(otp)) {
                cache.evict(email);
                return true;
            }
        }
        return false;
    }
}

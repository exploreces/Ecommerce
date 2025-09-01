package com.ecom.inventory.services;

import com.ecom.inventory.entity.User;
import com.ecom.inventory.exceptions.NotFoundException;
import com.ecom.inventory.exceptions.AlreadyExistsException;
import com.ecom.inventory.repositories.UserRepository;
import com.ecom.inventory.services.interfaces.UserServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signup(User user) {
        // check if email already exists
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new AlreadyExistsException("User already exists with email: " + user.getEmail());
        });
        return userRepository.save(user);
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

    public User updateProfile(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        // optional check: if updating email, ensure it doesn't belong to someone else
        userRepository.findByEmail(updatedUser.getEmail()).ifPresent(existing -> {
            if (!existing.getUserId().equals(userId)) {
                throw new AlreadyExistsException("Email already in use: " + updatedUser.getEmail());
            }
        });

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setAddress(updatedUser.getAddress());

        return userRepository.save(user);
    }
}

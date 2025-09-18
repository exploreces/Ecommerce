package com.ecom.inventory.services.interfaces;

import com.ecom.inventory.dtos.requestdto.PasswordDto;
import com.ecom.inventory.dtos.requestdto.SignUpRequestDto;
import com.ecom.inventory.dtos.requestdto.UserProfileUpdateDto;
import com.ecom.inventory.entity.User;

public interface UserServiceInterface {

    User signup(SignUpRequestDto user);
    String generateOtp(String email);
    User signin(String email, String password);
    User updateAddress(Long userId, String newAddress);
    User updateProfile(Long userId, UserProfileUpdateDto updatedUser);
    User getProfile(String email);
    User updatePassword(String email, PasswordDto password);
}

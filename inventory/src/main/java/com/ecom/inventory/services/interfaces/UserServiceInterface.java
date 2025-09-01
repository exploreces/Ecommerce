package com.ecom.inventory.services.interfaces;

import com.ecom.inventory.entity.User;

public interface UserServiceInterface {

    User signup(User user);
    User signin(String email, String password);
    User updateAddress(Long userId, String newAddress);
    User updateProfile(Long userId, User updatedUser);

}

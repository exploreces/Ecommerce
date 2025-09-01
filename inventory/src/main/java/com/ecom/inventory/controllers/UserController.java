package com.ecom.inventory.controllers;

import com.ecom.inventory.entity.User;
import com.ecom.inventory.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        User savedUser = userService.signup(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/signin")
    public ResponseEntity<User> signin(@RequestParam String email,
                                       @RequestParam String password) {
        User user = userService.signin(email, password);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/address")
    public ResponseEntity<User> updateAddress(@PathVariable Long userId,
                                              @RequestParam String newAddress) {
        User updated = userService.updateAddress(userId, newAddress);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<User> updateProfile(@PathVariable Long userId,
                                              @RequestBody User updatedUser) {
        User updated = userService.updateProfile(userId, updatedUser);
        return ResponseEntity.ok(updated);
    }
}

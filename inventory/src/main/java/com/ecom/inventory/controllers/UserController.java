package com.ecom.inventory.controllers;

import com.ecom.inventory.dtos.requestdto.PasswordDto;
import com.ecom.inventory.dtos.requestdto.SignUpRequestDto;
import com.ecom.inventory.dtos.requestdto.UserProfileUpdateDto;
import com.ecom.inventory.entity.User;
import com.ecom.inventory.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup/otp/{email}")
    public ResponseEntity<String> generateOtp(@PathVariable String email ) {
        System.out.println("otp processing ...");
        String otp = userService.generateOtp(email);
        return ResponseEntity.ok(otp);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequestDto user ) {
        User savedUser = userService.signup(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/signin")
    public ResponseEntity<User> signin(@RequestParam String email,
                                       @RequestParam String password) {
        User user = userService.signin(email, password);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/address/{userId}")
    public ResponseEntity<User> updateAddress(@PathVariable Long userId,
                                              @RequestParam String newAddress) {
        User updated = userService.updateAddress(userId, newAddress);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<User> updateProfile(@PathVariable Long userId,
                                              @RequestBody UserProfileUpdateDto updatedUser) {
        User updated = userService.updateProfile(userId, updatedUser);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/profile/email/{email}")
    public ResponseEntity<User> updatePassword(@PathVariable String email,
                                              @RequestBody PasswordDto password) {
        User updated = userService.updatePassword(email, password);
        return ResponseEntity.ok(updated);
    }


    @GetMapping("/profile/{email}")
    public ResponseEntity<User> Profile(@PathVariable String email) {
        User profile = userService.getProfile(email);
        return ResponseEntity.ok(profile);
    }
}

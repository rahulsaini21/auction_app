package com.auction.auth.controller;

import com.auction.auth.dto.UserProfile;
import com.auction.auth.exception.ResourceNotFoundException;
import com.auction.auth.model.User;
import com.auction.auth.repository.UserRepository;
import com.auction.auth.security.CurrentUser;
import com.auction.auth.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    // @PreAuthorize("hasRole('USER')")
    public UserProfile getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        logger.info("Fetching profile for current user: {}", userPrincipal.getUsername());
        return new UserProfile(
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                userPrincipal.getName(),
                userPrincipal.getEmail()
        );
    }

    @GetMapping("/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        logger.info("Fetching profile for username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found with username: {}", username);
                    return new ResourceNotFoundException("User", "username", username);
                });

        return new UserProfile(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail()
        );
    }

    @GetMapping("/id/{userId}")
    public UserProfile getUserById(@PathVariable("userId") String userId) {
        logger.info("Fetching profile for userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User", "id", userId);
                });

        return new UserProfile(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail()
        );
    }
}
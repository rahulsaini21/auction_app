package com.auction.auth.service;

import com.auction.auth.exception.ResourceNotFoundException;
import com.auction.auth.model.User;
import com.auction.auth.repository.UserRepository;
import com.auction.auth.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
                logger.info("usernameOrEmail: {}", usernameOrEmail);
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> {
                    logger.error("User not found with username or email: {}", usernameOrEmail);
                    return new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail);
                });

        logger.debug("User found: {}", user.getUsername());
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> {
                logger.error("User not found with id: {}", id);
                return new ResourceNotFoundException("User", "id", id);
            }
        );

        logger.debug("User found by ID: {}", user.getId());
        return UserPrincipal.create(user);
    }
}
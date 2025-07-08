package com.auction.auction.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.auction.auction.model.User;

@Service
public class AuthServiceClient {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthServiceClient(RestTemplate restTemplate,
                           @Value("${auth.service.base-url}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    public boolean validateToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            
            ResponseEntity<Void> response = restTemplate.exchange(
                authServiceUrl + "/api/auth/validate",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Void.class
            );
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public User getUserById(String userId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            ResponseEntity<User> response = restTemplate.exchange(
                authServiceUrl + "/api/users/id/" + userId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                User.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (Exception e) {
            // Log error
        }
        return null;
    }

    public User getUserFromToken(String token) {
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<User> response = restTemplate.exchange(
            authServiceUrl + "/api/users/me", // ➕ Add this endpoint to auth-service
            HttpMethod.GET,
            new HttpEntity<>(headers),
            User.class
        );

        return response.getBody();
    } catch (Exception e) {
        return null;
    }
}

}
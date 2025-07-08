package com.auction.bidding.service;

import com.auction.bidding.config.AuthServiceProperties;
import com.auction.bidding.config.UserServiceProperties;
import com.auction.bidding.dto.UserProfile;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceClient {

    private final RestTemplate restTemplate;
    private final AuthServiceProperties authServiceProperties;
    private final UserServiceProperties userServiceProperties;

    public AuthServiceClient(RestTemplate restTemplate, AuthServiceProperties authServiceProperties, UserServiceProperties userServiceProperties) {
        this.restTemplate = restTemplate;
        this.authServiceProperties = authServiceProperties;
        this.userServiceProperties = userServiceProperties;
    }

    public UserProfile getUserFromToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserProfile> response = restTemplate.exchange(
                userServiceProperties.getBaseUrl() + "/me",
                HttpMethod.GET,
                entity,
                UserProfile.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        return null;
    }

    public UserProfile getUserById(String userId) {
        return restTemplate.getForObject(
                authServiceProperties.getBaseUrl() + "/id/" + userId,
                UserProfile.class);
    }
}
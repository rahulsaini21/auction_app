package com.auction.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfile {
    private String id;
    private String username;
    private String name;
    private String email;
}
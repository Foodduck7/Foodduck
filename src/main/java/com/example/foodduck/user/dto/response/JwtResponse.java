package com.example.foodduck.user.dto.response;

import lombok.Getter;

@Getter
public class JwtResponse {
    private final String token;

    private final Long userId;

    private final String email;

    public JwtResponse(String token, Long userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
    }
}

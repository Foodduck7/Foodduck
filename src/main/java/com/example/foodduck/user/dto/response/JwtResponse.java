package com.example.foodduck.user.dto.response;

import lombok.Getter;

@Getter
public class JwtResponse {
    private final String accessToken;
    private final String refreshToken;
    private final Long userId;
    private final String email;

    public JwtResponse(String accessToken, String refreshToken, Long userId, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.email = email;
    }
}

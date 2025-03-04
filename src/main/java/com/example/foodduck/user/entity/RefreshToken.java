package com.example.foodduck.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    private Long userId;

    @Column(nullable = false)
    private String token;

    public RefreshToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}

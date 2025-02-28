package com.example.foodduck.user.dto.response;

import com.example.foodduck.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String role;


    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole().name();
    }
}

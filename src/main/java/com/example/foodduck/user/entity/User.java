package com.example.foodduck.user.entity;

import com.example.foodduck.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    private int storeCount = 0;

    public User(String name, String email, String password, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateUserName(String newUserName) {
        this.name = newUserName;
    }

    public void deleteUser() {
        this.isDeleted = true;
    }

    public void updateStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }
}

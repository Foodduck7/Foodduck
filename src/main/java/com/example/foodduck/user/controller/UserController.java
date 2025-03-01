package com.example.foodduck.user.controller;

import com.example.foodduck.user.dto.request.UserLoginRequest;
import com.example.foodduck.user.dto.request.UserPasswordUpdateRequest;
import com.example.foodduck.user.dto.request.UserJoinRequest;
import com.example.foodduck.user.dto.response.JwtResponse;
import com.example.foodduck.user.dto.response.UserResponse;
import com.example.foodduck.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserJoinRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId, @RequestParam String password) {
        userService.deleteUser(userId, password);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    // 비밀번호 변경
    @PatchMapping("/{userId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long userId, @Valid @RequestBody UserPasswordUpdateRequest request) {
        userService.updatePassword(userId, request);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    // 로그아웃
    public ResponseEntity<String> logout(@RequestParam Long userId) {
        userService.logout(userId);
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshAccessToken(@RequestParam Long userId, @RequestParam String refreshToken) {
        return ResponseEntity.ok(userService.refreshAccessToken(userId, refreshToken));
    }
}

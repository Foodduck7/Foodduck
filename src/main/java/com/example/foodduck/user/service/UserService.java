package com.example.foodduck.user.service;

import com.example.foodduck.common.config.jwt.JwtUtil;
import com.example.foodduck.exception.InvalidCredentialException;
import com.example.foodduck.user.dto.request.UserJoinRequest;
import com.example.foodduck.user.dto.request.UserLoginRequest;
import com.example.foodduck.user.dto.request.UserPasswordUpdateRequest;
import com.example.foodduck.user.dto.response.JwtResponse;
import com.example.foodduck.user.dto.response.UserResponse;
import com.example.foodduck.user.entity.RefreshToken;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.entity.UserRole;
import com.example.foodduck.user.repository.RefreshTokenRepository;
import com.example.foodduck.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //회원 가입
    public UserResponse registerUser(UserJoinRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }
        // owner랑 user만 되도록 세팅해놔서 admin 을 거부하고 있어서 추가해줘야함. 또한, user를 기본값으로 세팅.
        UserRole role;
        try {
            role = UserRole.valueOf(request.getRole().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            role = UserRole.USER;
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                role
        );


        userRepository.save(user);
        return new UserResponse(user);
    }

    //로그인 (accessToken + refreshToken 발급)
    public JwtResponse loginUser(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

//        // 비밀번호 확인 로그 추가
//        System.out.println("로그인 시도한 비밀번호"+request.getPassword());
//        System.out.println("db에 저장된 암호화된 비밀번호"+user.getPassword());


        if (user.isDeleted()) {
            throw new InvalidCredentialException("탈퇴한 계정입니다. 다시 가입해주세요.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        //리프레시 토큰을 DB에 저장 (기존 리프레시 토큰이 있으면 삭제 후 저장)
        refreshTokenRepository.deleteByUserId(user.getId());
        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));

        return new JwtResponse(accessToken, refreshToken, user.getId(), user.getEmail());
    }

    //로그아웃 (리프레시 토큰 삭제)
    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    //액세스 토큰 재발급 (리프레시 토큰 검증)
    public JwtResponse refreshAccessToken(Long userId, String refreshToken) {
        Optional<RefreshToken> storedToken = refreshTokenRepository.findByUserId(userId);
        if (storedToken.isEmpty() || !storedToken.get().getToken().equals(refreshToken)) {
            throw new InvalidCredentialException("유효하지 않은 리프레시 토큰입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId());

        return new JwtResponse(newAccessToken, refreshToken, user.getId(), user.getEmail());
    }

    //soft delete (회원 탈퇴)
    @Transactional
    public void deleteUser(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        user.deleteUser();
        refreshTokenRepository.deleteByUserId(userId);
        userRepository.save(user);
    }

    //비밀번호 변경
    @Transactional
    public void updatePassword(Long userId, UserPasswordUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }

//        // 비밀번호 변경 찍어보려고 추가 (비밀번호 변경 후 로그인시 500에러 발생 해결하려고)
//        System.out.println("예전 비밀번호 " + user.getPassword());

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));

//        // 바뀐 비밀번호
//        System.out.println("바뀐 비번 "+ user.getPassword());

        // 기존 리프레시 토큰 삭제 -> 이렇게 함으로써 자동 로그아웃이 됨.
        refreshTokenRepository.deleteByUserId(userId);

        userRepository.save(user);
    }
}

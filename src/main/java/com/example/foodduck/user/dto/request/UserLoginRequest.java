package com.example.foodduck.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {
    @Email(message="유효한 이메일 주소를 입력해주세요")
    private String email;
    @NotBlank(message = "비밀번호는 필수로 입력해주세요")
    private String password;
}

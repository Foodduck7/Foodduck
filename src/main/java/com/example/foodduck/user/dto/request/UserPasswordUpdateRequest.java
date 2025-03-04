package com.example.foodduck.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordUpdateRequest {
    @NotBlank(message = "기존 비밀번호를 입력해주셔야합니다.")
    private String oldPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    private String newPassword;

}

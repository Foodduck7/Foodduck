package com.example.foodduck.user.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {
    @NotBlank(message="이름은 필수 입력 항목이에요")
    private String name;

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "비밀번호는 최소 8자 이상이며, 영문+숫자+특수문자를 포함해야 합니다.")
    private String password;

    private String role;


}

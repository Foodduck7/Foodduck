package com.example.foodduck.user;

import com.example.foodduck.user.dto.request.UserJoinRequest;
import com.example.foodduck.user.dto.response.UserResponse;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.entity.UserRole;
import com.example.foodduck.user.repository.UserRepository;
import com.example.foodduck.user.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nl.altindag.log.LogCaptor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private LogCaptor logCaptor;

    @BeforeEach
    void setUp() {
        logCaptor = LogCaptor.forClass(UserService.class);
    }

    /**
     * 회원가입 성공 테스트 (BDD 스타일 적용)
     */
    @Test
    void 회원가입을_정상적으로_진행한다() {
        // Given
        UserJoinRequest request = new UserJoinRequest("testUser", "test@test.com", "Test@1234", "USER");
        User mockUser = new User("testUser", "test@test.com", "암호화된pw", UserRole.USER);

        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("암호화된pw");
        given(userRepository.save(any(User.class))).willReturn(mockUser);

        // When
        UserResponse response = userService.registerUser(request);

        // Then
        assertNotNull(response);
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getRole()).isEqualTo("USER");

        verify(userRepository, times(1)).save(any(User.class));

        assertThat(logCaptor.getInfoLogs()).contains("회원가입 성공: " + request.getEmail());
    }

    /**
     * 회원가입 실패 - 이메일 중복 예외 발생 확인
     */
    @Test
    void 회원가입_중_이메일이_중복되면_예외가_발생한다() {
        // Given
        UserJoinRequest request = new UserJoinRequest("testUser", "test@test.com", "Test@1234", "USER");
        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

        // When
        Throwable thrown = catchThrowable(() -> userService.registerUser(request));

        // Then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일입니다");

        assertThat(logCaptor.getErrorLogs()).contains("회원가입 실패: 이미 존재하는 이메일입니다");
    }

    @Test
    void 회원가입_중_잘못된_역할이_입력되면_기본값_User가_적용되도록(){
        //given
        UserJoinRequest request = new UserJoinRequest("testUser", "test@test.com", "Test12345678", "USER");
        User testUser = new User("testUser", "test@test.com", "암호화된pw", UserRole.USER);

        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("암호화된pw");
        given(userRepository.save(any(User.class))).willReturn(testUser);

        //when
        UserResponse userResponse = userService.registerUser(request);

        //then
        org.junit.jupiter.api.Assertions.assertNotNull(userResponse);
        Assertions.assertThat(userResponse.getRole()).isEqualTo("USER");

        verify(userRepository, times(1)).save(any(User.class));

        Assertions.assertThat(logCaptor.getInfoLogs()).contains("회원가입 성공: " + request.getEmail());
    }

    @Test
    void 회원가입_중_비밀번호_형식이_올바르지_않으면_예외가_발생한다() {
        //given
        UserJoinRequest request = new UserJoinRequest("testUser", "test@test.com", "password123", "USER");

        //when
        //validator를 사용하여 dto의 유효성 검증 수행
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserJoinRequest>> violations = validator.validate(request);

        //then
        assertThat(violations).isNotEmpty();//유효성 검사에서 오류가 발생했는지 확인
        assertThat(violations.iterator().next().getMessage()).isEqualTo("비밀번호는 최소 8자 이상이며, 영문+숫자+특수문자를 포함해야 합니다.");
    }
}


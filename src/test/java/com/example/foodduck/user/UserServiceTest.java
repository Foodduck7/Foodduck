package com.example.foodduck.user;

import com.example.foodduck.common.config.jwt.JwtUtil;
import com.example.foodduck.exception.custom.InvalidCredentialException;
import com.example.foodduck.user.dto.request.UserJoinRequest;
import com.example.foodduck.user.dto.request.UserLoginRequest;
import com.example.foodduck.user.dto.response.JwtResponse;
import com.example.foodduck.user.dto.response.UserResponse;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.entity.UserRole;
import com.example.foodduck.user.repository.RefreshTokenRepository;
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

    @Mock
    private JwtUtil jwtUtil;
    /*
    <fix>
    Cannot invoke "com.example.foodduck.common.config.jwt.JwtUtil.generateAccessToken(String, java.lang.Long)"
    because "this.jwtUtil" is null
    UserService에서 jwtUtil.generateAccessToken()을 호출하는데, jwtUtil이 null로 설정되어 있으며,
    UserServiceTest에서 JwtUtil을 @Mock으로 주입하지 않았기 때문이라고 판단.
    <refactor>
    JwtUtil을 @Mock으로 추가
    */

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private UserService userService;

    private LogCaptor logCaptor;

    @BeforeEach
    void setUp() {
        logCaptor = LogCaptor.forClass(UserService.class);
    }

    /**
     * 회원가입 테스트
     */
    @Test
    void 회원가입_성공() {
        //given
        UserJoinRequest request = new UserJoinRequest("testUser", "test@test.com", "Test@1234", "USER");
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("암호화된pw");
        User mockUser = new User("testUser", "test@test.com", "암호화된pw", UserRole.USER);
        given(userRepository.save(any(User.class))).willReturn(mockUser);

        //when
        UserResponse response = userService.registerUser(request);

        //then
        assertNotNull(response);
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getRole()).isEqualTo("USER");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void 회원가입_중_이메일_중복시_예외발생() {
        //given
        UserJoinRequest request = new UserJoinRequest("testUser", "test@test.com", "Test@1234", "USER");
        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

        //when
        Throwable thrown = catchThrowable(() -> userService.registerUser(request));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일입니다");
    }

    @Test
    void 회원가입_중_잘못된_역할입력시_USER_기본값_적용() {
        //given
        UserJoinRequest request = new UserJoinRequest("testUser", "test@test.com", "Test@1234", "INVALID_ROLE");
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("암호화된pw");
        User mockUser = new User("testUser", "test@test.com", "암호화된pw", UserRole.USER);
        given(userRepository.save(any(User.class))).willReturn(mockUser);

        //when
        UserResponse response = userService.registerUser(request);

        //then
        assertNotNull(response);
        assertThat(response.getRole()).isEqualTo("USER");
    }

    /**
     * 로그인 테스트
     */
    void 로그인_성공() {
        //given
        UserLoginRequest request = new UserLoginRequest("test@test.com", "Test@1234");
        User mockUser = new User("testUser", "test@test.com", "암호화된pw", UserRole.USER);

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(mockUser));
        given(passwordEncoder.matches(request.getPassword(), mockUser.getPassword())).willReturn(true);

        given(jwtUtil.generateAccessToken(anyString(), anyLong())).willReturn("mockAccessToken");
        given(jwtUtil.generateRefreshToken(anyLong())).willReturn("mockRefreshToken");

        //when
        JwtResponse response = userService.loginUser(request);

        //then
        assertNotNull(response);
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getAccessToken()).isEqualTo("mockAccessToken");
        assertThat(response.getRefreshToken()).isEqualTo("mockRefreshToken");

        assertThat(logCaptor.getInfoLogs()).contains("로그인 성공: " + request.getEmail());
    }

    @Test
    void 로그인_중_비밀번호_불일치시_예외발생() {
        //given
        UserLoginRequest request = new UserLoginRequest("test@test.com", "wrongpassword");
        User mockUser = new User("testUser", "test@test.com", "암호화된pw", UserRole.USER);
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(mockUser));
        given(passwordEncoder.matches(request.getPassword(), mockUser.getPassword())).willReturn(false);

        //when
        Throwable thrown = catchThrowable(() -> userService.loginUser(request));

        //then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    void 로그인_중_삭제된_계정_예외발생() {
        //given
        UserLoginRequest request = new UserLoginRequest("test@test.com", "Test@1234");
        User mockUser = new User("testUser", "test@test.com", "암호화된pw", UserRole.USER);
        mockUser.deleteUser();
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(mockUser));

        //when
        Throwable thrown = catchThrowable(() -> userService.loginUser(request));

        //then
        assertThat(thrown).isInstanceOf(InvalidCredentialException.class)
                .hasMessage("탈퇴한 계정입니다. 다시 가입해주세요.");
    }

    @Test
    void 로그아웃_성공(){

    }

    @Test
    void 액세스_토큰_재발급_성공(){

    }

    @Test
    void 유효하지_않은_리프레시_토큰_예외발생(){

    }

    @Test
    void 회원_탈퇴_성공(){

    }

    @Test
    void 회원_탈퇴_중_존재하지_않는_유저_예외발생처리(){

    }

    @Test
    void 회원_탈퇴_중_비밀번호_불일치_예외발생() {

    }

    @Test
    void 비밀번호_변경_성공(){

    }
}
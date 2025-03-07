package com.example.foodduck.user;

import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.entity.UserRole;
import com.example.foodduck.user.repository.UserRepository;
import com.example.foodduck.user.service.CustomizingUserDetailsService;
import com.example.foodduck.user.service.UserService;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomizingUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomizingUserDetailsService userDetailsService;

    private LogCaptor logCaptor;

    @BeforeEach
    void setUp() {
        logCaptor = LogCaptor.forClass(UserService.class);

    }

    /**
     * 유저 이메일로 정상적으로 `UserDetails`를 반환하는지 검증 과정
     */
    @Test
    void 유저_이메일로_UserDetails를_정상적으로_가져온다() {
        //given
        String email = "test@example.com";
        User mockUser = new User("테스트 유저", email, "암호화된비밀번호", UserRole.USER);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(mockUser));

        //when
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        //then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo(mockUser.getPassword());
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");

        verify(userRepository).findByEmail(email);
    }

    /**
     * 존재하지 않는 유저 이메일을 조회할 때, 예외가 발생하는지 검증 과정
     */
    @Test
    void 존재하지_않는_유저를_조회하면_예외가_발생한다() {
        //given
        String email = "notfoundUser@example.com";
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when
        Throwable thrown = catchThrowable(() -> userDetailsService.loadUserByUsername(email));

        //then
        assertThat(thrown)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다: " + email);
        verify(userRepository).findByEmail(email);
    }
}

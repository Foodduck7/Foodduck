package com.example.foodduck.common.config.jwt;


import com.example.foodduck.common.filter.JwtAuthenticationFilter;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.repository.UserRepository;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/shoppingCarts/{id}/orders/{orderId}").permitAll()
                        .requestMatchers("/users/register", "/users/login", "menus", "menus/{menuId}").permitAll()
                        .requestMatchers("/users/logout").authenticated()
                        .requestMatchers("/stores/{userId}", "/stores/{storeId}").hasAuthority("ROLE_OWNER")
                        .requestMatchers("/menus/{storeid}", "/menus/{menuId}/update", "menus/{menuId}/delete").hasAuthority("ROLE_OWNER")
                        .requestMatchers("/shoppingCarts/{id}/orders/request").hasAuthority("ROLE_USER")
                        .requestMatchers("/shoppingCarts/create", "/shoppingCarts/add", "/shoppingCarts/remove").hasAuthority("ROLE_USER")
                        .requestMatchers("/shoppingCarts/{id}/orders/status").hasAuthority("ROLE_OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/shoppingCarts/{id}/orders/{orderId}").hasAuthority("ROLE_OWNER")
                        .requestMatchers("/admin/**").hasRole("ADMIN") // hasAuthority("ROLE_ADMIN")을 사용하고 있어 관리자 접근이 차단됨. 수정
                        .requestMatchers(
                                "/menus/{storeid}",
                                "/menus/{menuId}/update",
                                "/menus/{menuId}/delete",
                                "/menus/{menuId}/options",
                                "/menus/options/{optionId}/update",
                                "/menus/options/{optionId}/delete"
                        ).hasRole("OWNER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); //필터 직접 등록

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public Filter jwtAuthenticationFilter() { //필터를Bean으로 등록
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }
}

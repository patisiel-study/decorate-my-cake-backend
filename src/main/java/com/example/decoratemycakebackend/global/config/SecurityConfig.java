package com.example.decoratemycakebackend.global.config;

import com.example.decoratemycakebackend.global.auth.JwtAuthenticationFilter;
import com.example.decoratemycakebackend.global.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                // Rest API이므로 basic auth 및 csrf 보안을 사용하지 않음
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // JWT를 사용하므로 세션 사용하지 않음
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(requests -> {
                    // 해당 API에 대해서는 모든 요청을 허가
                    requests.requestMatchers("/member/login", "/member/signup",
                            "/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    // MEMBER 권한이 있는 경우에만 요청 허가
                    requests.requestMatchers("/member/**", "/cake/**", "/candle/**," +
                            "/friend/**", "/menu/**").hasRole("MEMBER");
                    // 그 외에 모든 요청도 인증이 필요하도록 설정
                    requests.anyRequest().authenticated();
                })
                // JWT 인증을 위해 직접 구현한 필터를 먼저 거치고 그 후에 UsernamePasswordAuthenticatedFilter 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

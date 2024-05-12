package com.example.decoratemycakebackend.global.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. Request Header에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        // 로그아웃 요청일 경우 redis에서 refreshToken 제거
        if (requestURI.equals("/member/logout")) {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getAuthentication(token).getName();
                jwtTokenProvider.deleteRefreshToken(username);
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_OK);
            } else {
                // 토큰이 없거나 유효하지 않은 경우 에러 처리
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

            return;
        }

        try {
            // 2. validateToken으로 토큰 유효성 검사
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 유효하면 Authentication 객체를 가지고 와서 SecurityContext에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            // JWT 토큰이 만료된 경우 예외 처리
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String errorResponse = "{\"error\": \"JWT token has expired\"}";
            response.getWriter().write(errorResponse);
            return;
        }
        // 3. 다음 필터로 요청을 전달한다.
        chain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출(Bearer뒤에 문자 부분 들고옴)
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}

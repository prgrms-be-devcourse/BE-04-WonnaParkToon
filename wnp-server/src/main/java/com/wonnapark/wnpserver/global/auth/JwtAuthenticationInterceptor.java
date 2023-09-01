package com.wonnapark.wnpserver.global.auth;

import com.wonnapark.wnpserver.domain.auth.TokenConstants;
import com.wonnapark.wnpserver.domain.auth.application.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenService jwtTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = parseToken(request, HttpHeaders.AUTHORIZATION);
        String path = request.getRequestURI();

        // 아래에 해당하는 경우가 아예 없겠군
        if (path.equals("/api/v1/auth/reissue")) {
            String refreshToken = parseToken(request, TokenConstants.REFRESH_TOKEN);
            try {
                jwtTokenService.isValidToken(refreshToken);
            } catch (ExpiredJwtException expiredJwtException) {
                // TODO: 2023-09-01 강제 로그아웃 시키기 
            }
        }

        // TODO: 2023-08-31 재발급 리다이렉션 보내기
        try {
            jwtTokenService.isValidToken(accessToken);
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            response.sendRedirect("/api/v1/auth/reissue");
            return false;
        }
    }

    private String parseToken(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return token;
    }

    private boolean validateAccessToken(String accessToken, HttpServletResponse response) throws IOException {
        try {
            return jwtTokenService.isValidToken(accessToken);
        } catch (ExpiredJwtException expiredJwtException) {
            response.sendRedirect("/api/v1/auth/reissue");
            return false;
        }
    }

    private boolean validateRefreshToken(String refreshToken, HttpServletResponse response) throws IOException {
        try {
            if (jwtTokenService.isValidToken(refreshToken)) {
                return jwtTokenService.existsRefreshTokenByUserId(refreshToken);
            }
            return false;
        } catch (ExpiredJwtException expiredJwtException) {
            response.sendRedirect("/api/v1/auth/reissue");
            return false;
        }
    }

}

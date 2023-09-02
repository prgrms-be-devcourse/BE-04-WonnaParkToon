package com.wonnapark.wnpserver.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonnapark.wnpserver.domain.auth.TokenConstants;
import com.wonnapark.wnpserver.domain.auth.application.JwtTokenService;
import com.wonnapark.wnpserver.domain.auth.dto.RefreshTokenResponse;
import com.wonnapark.wnpserver.domain.auth.exception.JwtInvalidException;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.global.response.ErrorCode;
import com.wonnapark.wnpserver.global.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] blackList = {
                "/api/auth/kakao",
                "/h2-console"
        };
        String path = request.getRequestURI();
        return Arrays.stream(blackList).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String accessToken = parseToken(request, HttpHeaders.AUTHORIZATION);
        try {
            if (jwtTokenService.isValidToken(accessToken)){
                if (path.equals("/api/v1/auth/reissue")) {
                    String refreshToken = parseToken(request, TokenConstants.REFRESH_TOKEN);
                    UserInfo userInfo = jwtTokenService.extractUserInfo(accessToken);
                    if (!validateRefreshToken(refreshToken, userInfo.userId())) {
                        setErrorResponse(response, ErrorCode.BAD_REQUEST);
                        log.warn("토큰 예외 정보 : {}", response);
                        return;
                    }
                }
                filterChain.doFilter(request, response);
            }
        } catch (JwtInvalidException jwtInvalidException) {
            setErrorResponse(response, jwtInvalidException.getErrorCode());
            log.warn("토큰 예외 정보 : {}", response);
        }
    }

    private String parseToken(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }

    private boolean validateRefreshToken(String refreshToken, Long userId) {
        if (jwtTokenService.isValidToken(refreshToken)) {
            RefreshTokenResponse refreshTokenResponse = jwtTokenService.findRefreshTokenByUserId(userId);// 요청 토큰과 저장된 토큰이 같은지 검증해야함
            if (refreshTokenResponse.refreshToken().equals(refreshToken)) {
                return true;
            }
        }
        return false;
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
        response.setStatus(errorCode.getValue());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = ErrorResponse.create(errorCode);
        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(json);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}

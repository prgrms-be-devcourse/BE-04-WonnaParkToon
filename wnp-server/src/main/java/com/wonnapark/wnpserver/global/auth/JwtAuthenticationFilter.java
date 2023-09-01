package com.wonnapark.wnpserver.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonnapark.wnpserver.domain.auth.TokenConstants;
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
        String refreshToken = parseToken(request, TokenConstants.REFRESH_TOKEN);

        if (isTokenNull(accessToken, response)) {
            return;
        }
        if (path.equals("/api/v1/auth/reissue")) {
            if (isTokenNull(refreshToken, response)) {
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }

    private boolean isTokenNull(String token, HttpServletResponse response) {
        if (token == null) {
            setErrorResponse(response);
            return true;
        }
        return false;
    }

    private void setErrorResponse(HttpServletResponse response) { // 메서드 명 마음에 안듬;
        response.setStatus(ErrorCode.TOKEN_NOT_FOUND.getValue());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = ErrorResponse.create(ErrorCode.TOKEN_NOT_FOUND);
        try {
            String json = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(json);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}

package com.wonnapark.wnpserver.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String REFRESH_TOKEN = "Refresh-Token";
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] blackList = {
                "/anonymous",
                "/api/auth/kakao",
                "/h2-console"
        };
        String path = request.getRequestURI();
        return Arrays.stream(blackList).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = parseToken(request, HttpHeaders.AUTHORIZATION);
        String refreshToken = parseToken(request, REFRESH_TOKEN);
        log.info("인증 필터 호출 Api -> {}", request.getRequestURI());

        if (isTokenNull(accessToken, response)) {
            return;
        }
        if (request.getRequestURI().equals("/api/auth/reissue")) {
            if (isTokenNull(refreshToken, response)) {
                return;
            }
        }
        filterChain.doFilter(request, response);
        log.info("인증 필터 종료 Api -> {}", request.getRequestURI());
    }

    private String parseToken(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        return token;
    }

    private boolean isTokenNull(String token, HttpServletResponse response) {
        if (token == null) {
            response.setStatus(ErrorCode.TOKEN_NOT_FOUND.getValue());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ErrorResponse errorResponse = ErrorResponse.create(ErrorCode.TOKEN_NOT_FOUND);
            try {
                String json = objectMapper.writeValueAsString(errorResponse);
                response.getWriter().write(json);
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
            return true;
        }
        return false;
    }
}

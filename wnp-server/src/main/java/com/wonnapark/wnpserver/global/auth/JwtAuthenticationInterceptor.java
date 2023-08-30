package com.wonnapark.wnpserver.global.auth;

import com.wonnapark.wnpserver.domain.auth.application.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenService jwtTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = parseToken(request, HttpHeaders.AUTHORIZATION);
        String path = request.getRequestURI();
        if (jwtTokenService.isValidToken(accessToken)) {


        }
        if (request.getRequestURI().equals("/api/auth/reissue")) {
            if (jwtTokenService.isValidToken())
        }


        return true;
    }

    private String parseToken(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        return token;
    }

}

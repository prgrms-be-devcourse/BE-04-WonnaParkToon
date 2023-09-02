package com.wonnapark.wnpserver.global.auth;

import com.wonnapark.wnpserver.domain.auth.TokenConstants;
import com.wonnapark.wnpserver.domain.auth.application.JwtTokenService;
import com.wonnapark.wnpserver.global.common.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenService jwtTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = parseToken(request, HttpHeaders.AUTHORIZATION);
        UserInfo userInfo = jwtTokenService.extractUserInfo(accessToken);
        String path = request.getRequestURI();

        if (path.equals("/api/v1/auth/reissue")) {
            String refreshToken = parseToken(request, TokenConstants.REFRESH_TOKEN);
            if (!validateRefreshToken(refreshToken, userInfo.userId())) return false;
        }
        return jwtTokenService.isValidToken(accessToken);
    }

    private String parseToken(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return token;
    }

    private boolean validateRefreshToken(String refreshToken, Long userId) {
        if (jwtTokenService.isValidToken(refreshToken)) {
//            jwtTokenService.(userId); // 요청 토큰과 저장된 토큰이 같은지 검증해야함
        }
        return false;
    }

}

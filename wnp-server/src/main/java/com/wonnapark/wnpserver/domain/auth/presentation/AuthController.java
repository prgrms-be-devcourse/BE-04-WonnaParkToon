package com.wonnapark.wnpserver.domain.auth.presentation;

import com.wonnapark.wnpserver.domain.auth.application.JwtTokenService;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.global.auth.Authentication;
import com.wonnapark.wnpserver.global.auth.AuthenticationContextHolder;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenService jwtTokenService;

    @GetMapping("reissue")
    public ApiResponse<AuthTokenResponse> reissueAuthToken() {
        Authentication authentication = AuthenticationContextHolder.getAuthentication();
        AuthTokenResponse authTokenResponse = jwtTokenService.generateAuthToken(AuthTokenRequest.from(authentication));
        return ApiResponse.from(authTokenResponse);
    }

    // TODO: 2023-09-04 로그아웃 구현 토큰 삭제 및 블랙리스트 처리

}

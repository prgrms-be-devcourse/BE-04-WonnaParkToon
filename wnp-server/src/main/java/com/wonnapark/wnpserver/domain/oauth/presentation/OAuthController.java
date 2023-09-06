package com.wonnapark.wnpserver.domain.oauth.presentation;

import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.domain.oauth.application.OAuthLoginService;
import com.wonnapark.wnpserver.domain.oauth.application.OAuthLogoutService;
import com.wonnapark.wnpserver.domain.oauth.dto.request.KakaoLoginRequest;
import com.wonnapark.wnpserver.global.auth.Authorized;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthLoginService oauthLoginService;
    private final OAuthLogoutService oAuthLogoutService;

    @GetMapping("/")

    @GetMapping("/kakao")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AuthTokenResponse> loginWithKakao(@ModelAttribute KakaoLoginRequest request) {
        return ApiResponse.from(oauthLoginService.login(request));
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Authorized UserInfo userInfo, HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        oAuthLogoutService.logout(userInfo.userId(), accessToken);
    }

}

package com.wonnapark.wnpserver.domain.oauth.presentation;

import com.wonnapark.wnpserver.domain.oauth.application.OAuthLoginService;
import com.wonnapark.wnpserver.domain.oauth.dto.KakaoLoginRequest;
import com.wonnapark.wnpserver.domain.oauth.token.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuthController {

    private final OAuthLoginService oauthLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<AuthToken> loginKakao(@RequestBody KakaoLoginRequest request) {
        return ResponseEntity.ok(oauthLoginService.login(request));
    }

}

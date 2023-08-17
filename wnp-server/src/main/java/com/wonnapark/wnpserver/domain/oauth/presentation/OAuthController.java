package com.wonnapark.wnpserver.domain.oauth.presentation;

import com.wonnapark.wnpserver.domain.oauth.application.OAuthLoginService;
import com.wonnapark.wnpserver.domain.oauth.dto.request.KakaoLoginRequest;
import com.wonnapark.wnpserver.domain.auth.dto.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuthController {

    private final OAuthLoginService oauthLoginService;

    @GetMapping("/kakao")
    public ResponseEntity<AuthToken> loginkakao(@ModelAttribute KakaoLoginRequest request) {
        return ResponseEntity.ok(oauthLoginService.login(request));
    }

}

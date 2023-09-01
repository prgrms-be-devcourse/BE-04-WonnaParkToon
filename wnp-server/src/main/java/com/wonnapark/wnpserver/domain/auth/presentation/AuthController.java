package com.wonnapark.wnpserver.domain.auth.presentation;

import com.wonnapark.wnpserver.domain.auth.application.JwtTokenService;
import com.wonnapark.wnpserver.domain.user.infrastructure.UserRepository;
import com.wonnapark.wnpserver.global.common.Authorized;
import com.wonnapark.wnpserver.global.common.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    @GetMapping("/reissue-all")
    public void 재발급(@Authorized UserInfo userInfo) {
        jwtTokenService.generateAuthToken()
    }

}

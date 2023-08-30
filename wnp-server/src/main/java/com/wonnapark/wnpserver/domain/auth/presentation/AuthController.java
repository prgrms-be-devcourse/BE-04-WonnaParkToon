package com.wonnapark.wnpserver.domain.auth.presentation;

import com.wonnapark.wnpserver.domain.user.User;
import com.wonnapark.wnpserver.domain.user.infrastructure.UserRepository;
import com.wonnapark.wnpserver.global.common.Authorized;
import com.wonnapark.wnpserver.global.common.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @GetMapping("/reissue")
    public void reissue(@Authorized UserInfo userInfo) {
        System.out.println("userInfo = " + userInfo.userId());
        System.out.println("userInfo = " + userInfo.birthYear());
        Optional<User> byId = userRepository.findById(userInfo.userId());
        System.out.println("byId = " + byId.get());
    }

}

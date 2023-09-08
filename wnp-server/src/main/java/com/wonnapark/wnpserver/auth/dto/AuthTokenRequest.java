package com.wonnapark.wnpserver.auth.dto;

import com.wonnapark.wnpserver.user.Role;
import com.wonnapark.wnpserver.global.auth.Authentication;

public record AuthTokenRequest(
        Long userId,
        int age,
        Role role
) {
    public static AuthTokenRequest from(Authentication authentication) {
        return new AuthTokenRequest(authentication.userId(), authentication.age(), authentication.role());
    }

}

package com.wonnapark.wnpserver.domain.auth.dto;

import com.wonnapark.wnpserver.domain.user.Role;

public record AuthTokenRequest(
        Long userId,
        int age,
        Role role
) {
}

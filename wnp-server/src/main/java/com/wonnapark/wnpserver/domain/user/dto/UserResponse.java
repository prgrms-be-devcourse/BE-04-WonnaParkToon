package com.wonnapark.wnpserver.domain.user.dto;

import com.wonnapark.wnpserver.domain.user.OAuthProvider;
import com.wonnapark.wnpserver.domain.user.User;

public record UserResponse(
        Long id,
        Long providerId,
        OAuthProvider platform,
        String nickname,
        String birthYear,
        String gender
){
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(),
                user.getProviderId(),
                user.getPlatform(),
                user.getNickname(),
                user.getBirthYear(),
                user.getGender());
    }
}

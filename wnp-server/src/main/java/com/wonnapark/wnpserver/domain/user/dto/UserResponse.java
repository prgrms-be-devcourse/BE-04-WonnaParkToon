package com.wonnapark.wnpserver.domain.user.dto;

import com.wonnapark.wnpserver.domain.user.OAuthProvider;
import com.wonnapark.wnpserver.domain.user.User;

public record UserResponse(
        Long id,
        OAuthProvider platform,
        String nickname,
        String email,
        String ageRange,
        String gender
){
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(),
                user.getPlatform(),
                user.getNickname(),
                user.getEmail(),
                user.getAgeRange(),
                user.getGender());
    }
}

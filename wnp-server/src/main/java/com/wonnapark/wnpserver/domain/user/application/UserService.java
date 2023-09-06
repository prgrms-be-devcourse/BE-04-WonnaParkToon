package com.wonnapark.wnpserver.domain.user.application;

import com.wonnapark.wnpserver.domain.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.domain.user.OAuthProvider;
import com.wonnapark.wnpserver.domain.user.dto.UserResponse;

public interface UserService {

    UserResponse create(OAuthInfoResponse response);

    UserResponse findUserByProviderIdAndPlatform(Long providerId, OAuthProvider platform);
}

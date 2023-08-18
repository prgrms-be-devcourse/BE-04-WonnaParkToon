package com.wonnapark.wnpserver.domain.user.application;

import com.wonnapark.wnpserver.domain.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.domain.user.dto.UserResponse;

public interface UserService {

    UserResponse create(OAuthInfoResponse response);

    UserResponse findUserByProviderId(String providerId);
}

package com.wonnapark.wnpserver.domain.user.application;

import com.wonnapark.wnpserver.domain.oauth.dto.OAuthInfoResponse;
import com.wonnapark.wnpserver.domain.user.dto.UserResponse;

public interface UserService {

    Long create(OAuthInfoResponse response);

    UserResponse findUserByProviderId(String providerId);
}

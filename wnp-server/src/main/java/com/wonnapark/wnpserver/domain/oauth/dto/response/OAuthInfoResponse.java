package com.wonnapark.wnpserver.domain.oauth.dto.response;

import com.wonnapark.wnpserver.domain.user.OAuthProvider;
import com.wonnapark.wnpserver.domain.user.User;

public interface OAuthInfoResponse {

    Long getProviderId();

    String getNickname();

    OAuthProvider getOAuthProvider();

    String getBirthYear();

    String getGender();

    User toEntity();
}

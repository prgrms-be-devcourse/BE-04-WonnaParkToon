package com.wonnapark.wnpserver.domain.oauth.dto.response;

import com.wonnapark.wnpserver.domain.user.OAuthProvider;

public interface OAuthInfoResponse {

    Long getProviderId();

    String getNickname();

    OAuthProvider getOAuthProvider();

    int getAge();

    String getGender();
}

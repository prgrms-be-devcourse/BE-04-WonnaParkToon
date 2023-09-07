package com.wonnapark.wnpserver.domain.oauth.dto.response;

import com.wonnapark.wnpserver.domain.oauth.OAuthProvider;

public interface OAuthInfoResponse {

    String getProviderId();

    String getNickname();

    OAuthProvider getOAuthProvider();

    int getAge();

    String getGender();
}

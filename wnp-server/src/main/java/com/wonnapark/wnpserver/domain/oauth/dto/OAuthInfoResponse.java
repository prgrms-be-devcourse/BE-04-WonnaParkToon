package com.wonnapark.wnpserver.domain.oauth.dto;

import com.wonnapark.wnpserver.domain.user.OAuthProvider;
import com.wonnapark.wnpserver.domain.user.User;

public interface OAuthInfoResponse {

    String getEmail();

    String getNickname();

    OAuthProvider getOAuthProvider();

    String getAgeRange();

    String getGender();

    User toEntity();
}

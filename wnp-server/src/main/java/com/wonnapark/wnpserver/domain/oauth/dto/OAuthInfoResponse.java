package com.wonnapark.wnpserver.domain.oauth.dto;

import com.wonnapark.wnpserver.domain.user.OAuthProvider;

public abstract class OAuthInfoResponse {

    public abstract String getEmail();

    public abstract String getNickname();

    public abstract OAuthProvider getOAuthProvider();

    public abstract String getAgeRange();

    public abstract String getGender();
}

package com.wonnapark.wnpserver.domain.oauth.infrastructure;

import com.wonnapark.wnpserver.domain.oauth.dto.request.OAuthLoginRequest;
import com.wonnapark.wnpserver.domain.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.domain.user.OAuthProvider;

public abstract class OAuthApiClient {

    public abstract OAuthProvider getOAuthProvider();

    public abstract String requestAccessToken(OAuthLoginRequest request);

    public abstract OAuthInfoResponse requestOauthInfo(String accessToken);
}

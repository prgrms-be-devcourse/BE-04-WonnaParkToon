package com.wonnapark.wnpserver.domain.oauth.infrastructure.client;

import com.wonnapark.wnpserver.domain.oauth.OAuthProvider;
import com.wonnapark.wnpserver.domain.oauth.dto.request.OAuthLoginRequest;
import com.wonnapark.wnpserver.domain.oauth.dto.response.OAuthInfoResponse;

public interface OAuthApiClient {

    OAuthProvider getOAuthProvider();

    String requestAccessToken(OAuthLoginRequest request);

    OAuthInfoResponse requestOauthInfo(String accessToken);

}

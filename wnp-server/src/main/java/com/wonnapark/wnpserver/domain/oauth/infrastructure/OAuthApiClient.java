package com.wonnapark.wnpserver.domain.oauth.infrastructure;

import com.wonnapark.wnpserver.domain.oauth.dto.request.OAuthLoginRequest;
import com.wonnapark.wnpserver.domain.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.domain.user.OAuthProvider;

public interface OAuthApiClient {

    OAuthProvider getOAuthProvider();

    String requestAccessToken(OAuthLoginRequest request);

    OAuthInfoResponse requestOauthInfo(String accessToken);
}

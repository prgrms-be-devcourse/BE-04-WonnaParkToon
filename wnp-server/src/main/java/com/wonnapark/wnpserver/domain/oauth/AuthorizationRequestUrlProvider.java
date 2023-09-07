package com.wonnapark.wnpserver.domain.oauth;

public interface AuthorizationRequestUrlProvider {

    OAuthProvider oAuthProvider();

    String provideUrl();

}

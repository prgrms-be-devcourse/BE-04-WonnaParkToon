package com.wonnapark.wnpserver.domain.oauth.dto;

import com.wonnapark.wnpserver.domain.user.OAuthProvider;
import org.springframework.util.MultiValueMap;

public abstract class OAuthLoginRequest {

    public abstract OAuthProvider getOAuthProvider();

    public abstract MultiValueMap<String, String> makeBody();

}

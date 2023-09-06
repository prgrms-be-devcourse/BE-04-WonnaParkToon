package com.wonnapark.wnpserver.domain.oauth.dto.request;

import com.wonnapark.wnpserver.domain.oauth.OAuthProvider;
import org.springframework.util.MultiValueMap;

public abstract class OAuthLoginRequest {

    public abstract OAuthProvider getOAuthProvider();

    public abstract MultiValueMap<String, String> makeBody();

}

package com.wonnapark.wnpserver.domain.oauth.dto.request;

import com.wonnapark.wnpserver.domain.oauth.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginRequest {

    OAuthProvider getOAuthProvider();

    MultiValueMap<String, String> makeBody();

}

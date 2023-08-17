package com.wonnapark.wnpserver.domain.oauth.dto;

import com.wonnapark.wnpserver.domain.user.OAuthProvider;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class KakaoLoginRequest extends OAuthLoginRequest {

    private String code;

    public KakaoLoginRequest(String code) {
        this.code = code;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        return body;
    }
}

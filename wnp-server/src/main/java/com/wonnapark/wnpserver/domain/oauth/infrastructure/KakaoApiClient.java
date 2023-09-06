package com.wonnapark.wnpserver.domain.oauth.infrastructure;

import com.wonnapark.wnpserver.domain.auth.config.TokenConstants;
import com.wonnapark.wnpserver.domain.oauth.OAuthProvider;
import com.wonnapark.wnpserver.domain.oauth.config.OauthProperties;
import com.wonnapark.wnpserver.domain.oauth.dto.request.OAuthLoginRequest;
import com.wonnapark.wnpserver.domain.oauth.dto.response.KakaoInfoResponse;
import com.wonnapark.wnpserver.domain.oauth.dto.response.KakaoToken;
import com.wonnapark.wnpserver.domain.oauth.dto.response.OAuthInfoResponse;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {
    private static final String GRANT_TYPE = "authorization_code";
    private static final String EMPTY_SPACE = " ";

    private final RestTemplate restTemplate;
    private final OauthProperties oauthProperties;

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String requestAccessToken(OAuthLoginRequest params) {
        String url = oauthProperties.getKakao().getUrl().getAuthUrl() + OauthProperties.KAKAO_REQUEST_TOKEN_URI;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", oauthProperties.getKakao().getClientId());
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        KakaoToken response = restTemplate.postForObject(url, request, KakaoToken.class);

        Assert.notNull(response, "토큰 못 받았어요");
        // TODO: 2023-09-03 RestClientException 처리 또는 커스텀 예외로?
        return response.accessToken();
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = oauthProperties.getKakao().getUrl().getApiUrl() + OauthProperties.KAKAO_REQUEST_INFO_URL;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set(HttpHeaders.AUTHORIZATION, TokenConstants.BEARER_TYPE + EMPTY_SPACE + accessToken);

        HttpEntity<?> request = new HttpEntity<>(httpHeaders);
        return restTemplate.postForObject(url, request, KakaoInfoResponse.class);
        // TODO: 2023-09-03 RestClientException 처리 또는 커스텀 예외로?
    }

}

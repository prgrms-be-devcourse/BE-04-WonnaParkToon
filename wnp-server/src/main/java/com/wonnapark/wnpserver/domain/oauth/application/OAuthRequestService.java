package com.wonnapark.wnpserver.domain.oauth.application;

import com.wonnapark.wnpserver.domain.oauth.OAuthProvider;
import com.wonnapark.wnpserver.domain.oauth.dto.request.OAuthLoginRequest;
import com.wonnapark.wnpserver.domain.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.domain.oauth.infrastructure.client.OAuthApiClient;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuthRequestService {

    private final Map<OAuthProvider, OAuthApiClient> clients;

    public OAuthRequestService(Set<OAuthApiClient> clients) {
        this.clients = clients.stream()
                .collect(Collectors.toUnmodifiableMap(OAuthApiClient::getOAuthProvider, Function.identity()));
    }

    public OAuthInfoResponse requestInfo(OAuthLoginRequest request) {
        OAuthApiClient oAuthApiClient = getOAuthApiClient(request.getOAuthProvider());
        String accessToken = oAuthApiClient.requestAccessToken(request);
        return oAuthApiClient.requestOauthInfo(accessToken);
    }

    private OAuthApiClient getOAuthApiClient(OAuthProvider oAuthProvider) {
        return Optional.ofNullable(clients.get(oAuthProvider))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 로그인 방식입니다."));
    }
}

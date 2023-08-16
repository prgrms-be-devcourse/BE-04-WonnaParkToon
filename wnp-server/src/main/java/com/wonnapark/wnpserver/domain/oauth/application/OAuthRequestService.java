package com.wonnapark.wnpserver.domain.oauth.application;

import com.wonnapark.wnpserver.domain.oauth.dto.OAuthInfoResponse;
import com.wonnapark.wnpserver.domain.oauth.dto.OAuthLoginRequest;
import com.wonnapark.wnpserver.domain.oauth.infrastructure.OAuthApiClient;
import com.wonnapark.wnpserver.domain.user.OAuthProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuthRequestService {
    private final Map<OAuthProvider, OAuthApiClient> clients;

    public OAuthRequestService(List<OAuthApiClient> clients) {
        this.clients = clients.stream()
                .collect(Collectors.toUnmodifiableMap(OAuthApiClient::getOAuthProvider, Function.identity()));
    }

    public OAuthInfoResponse requestInfo(OAuthLoginRequest request) {
        OAuthApiClient client = clients.get(request.getOAuthProvider());
        String accessToken = client.requestAccessToken(request);
        return client.requestOauthInfo(accessToken);
    }
}

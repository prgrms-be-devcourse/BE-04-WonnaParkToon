package com.wonnapark.wnpserver.domain.oauth.application;

import com.wonnapark.wnpserver.domain.oauth.AuthorizationRequestUrlProvider;
import com.wonnapark.wnpserver.domain.oauth.OAuthProvider;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AuthorizationRequestService {

    private final Map<OAuthProvider, AuthorizationRequestUrlProvider> providers;

    public AuthorizationRequestService(Set<AuthorizationRequestUrlProvider> providers) {
        this.providers = providers.stream()
                .collect(Collectors.toUnmodifiableMap(AuthorizationRequestUrlProvider::oAuthProvider, Function.identity()));
    }

    public String provide(OAuthProvider oAuthProvider) {
        return getProivder(oAuthProvider).provideUrl();
    }

    private AuthorizationRequestUrlProvider getProivder(OAuthProvider oAuthProvider) {
        return Optional.ofNullable(providers.get(oAuthProvider))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 로그인 방식입니다."));
    }

}

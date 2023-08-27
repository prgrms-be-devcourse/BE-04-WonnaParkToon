package com.wonnapark.wnpserver.domain.oauth.application;

import com.wonnapark.wnpserver.domain.auth.application.JwtTokenService;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.domain.oauth.dto.request.OAuthLoginRequest;
import com.wonnapark.wnpserver.domain.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.domain.user.application.UserService;
import com.wonnapark.wnpserver.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final OAuthRequestService oAuthRequestService;

    public AuthTokenResponse login(OAuthLoginRequest request) {
        OAuthInfoResponse response = oAuthRequestService.requestInfo(request);
        AuthTokenRequest authTokenRequest = findOrCreateUser(response);
        return jwtTokenService.generateAuthToken(authTokenRequest);
    }

    private AuthTokenRequest findOrCreateUser(OAuthInfoResponse response) {
        try {
            UserResponse storedUser = userService.findUserByProviderIdAndPlatform(response.getProviderId(), response.getOAuthProvider());
            return new AuthTokenRequest(storedUser.id(), storedUser.birthYear());
        } catch (Exception e) {
            UserResponse storedUser = userService.create(response);
            return new AuthTokenRequest(storedUser.id(), storedUser.birthYear());
        }
    }

}

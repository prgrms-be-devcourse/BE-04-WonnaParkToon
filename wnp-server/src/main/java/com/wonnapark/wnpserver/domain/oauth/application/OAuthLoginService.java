package com.wonnapark.wnpserver.domain.oauth.application;

import com.wonnapark.wnpserver.domain.oauth.dto.response.OAuthInfoResponse;
import com.wonnapark.wnpserver.domain.oauth.dto.request.OAuthLoginRequest;
import com.wonnapark.wnpserver.domain.oauth.token.AuthToken;
import com.wonnapark.wnpserver.domain.oauth.token.AuthTokenGenerator;
import com.wonnapark.wnpserver.domain.user.application.UserService;
import com.wonnapark.wnpserver.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final UserService userService;
    private final AuthTokenGenerator authTokenGenerator;
    private final OAuthRequestService oAuthRequestService;

    public AuthToken login(OAuthLoginRequest request) {
        OAuthInfoResponse response = oAuthRequestService.requestInfo(request);
        Long memberId = findOrCreateUser(response);
        return authTokenGenerator.generate(memberId);
    }

    private Long findOrCreateUser(OAuthInfoResponse response) {
        try {
            UserResponse storedUser = userService.findUserByProviderId(response.getProviderId());
            return storedUser.id();
        } catch (Exception e) {
            return userService.create(response);
        }
    }

}

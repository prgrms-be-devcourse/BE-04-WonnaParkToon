package com.wonnapark.wnpserver.domain.oauth.application;

import com.wonnapark.wnpserver.domain.oauth.dto.OAuthInfoResponse;
import com.wonnapark.wnpserver.domain.oauth.dto.OAuthLoginRequest;
import com.wonnapark.wnpserver.domain.oauth.token.AuthToken;
import com.wonnapark.wnpserver.domain.oauth.token.AuthTokenGenerator;
import com.wonnapark.wnpserver.domain.user.User;
import com.wonnapark.wnpserver.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final UserRepository userRepository;
    private final AuthTokenGenerator authTokenGenerator;
    private final OAuthRequestService oAuthRequestService;

    public AuthToken login(OAuthLoginRequest request) {
        OAuthInfoResponse response = oAuthRequestService.request(request);
        Long memberId = findOrCreateMember(response);
        return authTokenGenerator.generate(memberId);
    }

    private Long findOrCreateMember(OAuthInfoResponse response) {
        return userRepository.findByEmail(response.getEmail())
                .map(User::getId)
                .orElseGet(() -> createMember(response));
    }

    private Long createMember(OAuthInfoResponse response) {
        User user = User.builder()
                .platform(response.getOAuthProvider())
                .nickname(response.getNickname())
                .email(response.getEmail())
                .ageRange(response.getAgeRange())
                .gender(response.getGender())
                .build();
        return userRepository.save(user).getId();
    }
}

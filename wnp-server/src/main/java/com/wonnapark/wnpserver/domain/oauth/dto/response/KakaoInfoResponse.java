package com.wonnapark.wnpserver.domain.oauth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wonnapark.wnpserver.domain.user.OAuthProvider;
import com.wonnapark.wnpserver.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoInfoResponse implements OAuthInfoResponse {

    private String id;

    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class KakaoAccount {
        private Profile profile;
        private String email;
        private String ageRange;
        private String gender;

        @Getter
        @NoArgsConstructor
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        private static class Profile {
            private String nickname;
        }
    }

    @Override
    public String getProviderId() {
        return id;
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String getAgeRange() {
        return kakaoAccount.ageRange;
    }

    @Override
    public String getGender() {
        return kakaoAccount.gender;
    }

    @Override
    public User toEntity() {
        return User.builder()
                .providerId(getId())
                .platform(getOAuthProvider())
                .nickname(getNickname())
                .ageRange(getAgeRange())
                .gender(getGender())
                .build();
    }
}

package com.wonnapark.wnpserver.domain.oauth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wonnapark.wnpserver.domain.user.OAuthProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoInfoResponse implements OAuthInfoResponse {

    private Long id;
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private static class KakaoAccount {
        private Profile profile;
        private String birthyear;
        private String gender;

        @Getter
        @NoArgsConstructor
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        private static class Profile {
            private String nickname;
        }
    }

    @Override
    public Long getProviderId() {
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
    public int getAge() {
        return getAgeFromBirthYear(kakaoAccount.birthyear);
    }

    @Override
    public String getGender() {
        return kakaoAccount.gender;
    }

    private int getAgeFromBirthYear(String birthYear) {
        int currentYear = LocalDateTime.now().getYear();
        int userBirthYear = Integer.parseInt(birthYear);
        return currentYear - userBirthYear;
    }
}

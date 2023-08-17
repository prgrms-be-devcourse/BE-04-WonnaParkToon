package com.wonnapark.wnpserver.domain.oauth.token;

import com.wonnapark.wnpserver.domain.auth.dto.AuthToken;
import com.wonnapark.wnpserver.domain.auth.application.AuthTokenGenerator;
import com.wonnapark.wnpserver.domain.auth.application.JwtTokenGenerator;
import com.wonnapark.wnpserver.global.config.OauthConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {AuthTokenGenerator.class, JwtTokenGenerator.class, OauthConfig.class})
class AuthTokenGeneratorTest {

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @DisplayName("JWT 토큰 생성 성공")
    @Test
    void generateTokenTest() {
        //given
        Long userId = 0L;

        //when
        AuthToken authToken = authTokenGenerator.generate(userId);

        //then
        assertThat(authToken.grantType()).isEqualTo("Bearer");
        assertThat(authToken.accessToken()).isNotBlank();
        assertThat(authToken.refreshToken()).isNotBlank();
        assertThat(authToken.expiredAt()).isNotNull();
    }

    @DisplayName("JWT 토큰 검증 성공")
    @Test
    void extractUserIdTest() {
        //given
        Long memberId = 0L;
        AuthToken authToken = authTokenGenerator.generate(memberId);
        String accessToken = authToken.accessToken();

        //when
        Long extractedMemberId = authTokenGenerator.extractUserId(accessToken);

        //then
        assertThat(extractedMemberId).isEqualTo(memberId);
    }
}
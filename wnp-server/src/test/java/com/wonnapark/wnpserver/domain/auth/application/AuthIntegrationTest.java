package com.wonnapark.wnpserver.domain.auth.application;

import com.wonnapark.wnpserver.domain.auth.config.TokenConstants;
import com.wonnapark.wnpserver.domain.auth.dto.AccessTokenResponse;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.domain.auth.dto.RefreshTokenResponse;
import com.wonnapark.wnpserver.domain.common.config.TestContainerConfig;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(TestContainerConfig.class)
@SpringBootTest
class AuthIntegrationTest {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AuthenticationResolver authenticationResolver;

    @Test
    @DisplayName("AccessToken 생성 성공 테스트")
    void generateAccessTokenTest() {
        // given
        AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);

        // when
        AccessTokenResponse accessTokenResponse = jwtTokenService.generateAccessToken(authTokenRequest);

        // then
        assertThat(accessTokenResponse.grantType()).isEqualTo(TokenConstants.BEARER_TYPE);
        assertThat(accessTokenResponse.accessTokenExpiresIn()).isEqualTo(ACCESS_TOKEN_EXPIRE_TIME / TokenConstants.MILLI_SECOND);
    }

    @Test
    @DisplayName("RefreshToken 생성 성공 테스트")
    void generateRefreshTokenTest() {
        // given
        AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);

        // when
        RefreshTokenResponse refreshTokenResponse = jwtTokenService.generateRefreshToken(authTokenRequest);

        // then
        assertThat(refreshTokenResponse.grantType()).isEqualTo(TokenConstants.BEARER_TYPE);
    }

    @Test
    @DisplayName("AuthToken 생성 성공 테스트")
    void generateAuthTokenTest() {
        // given
        AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);

        // when
        AuthTokenResponse authTokenResponse = jwtTokenService.generateAuthToken(authTokenRequest);

        // then
        assertThat(authTokenResponse.grantType()).isEqualTo(TokenConstants.BEARER_TYPE);
        assertThat(authTokenResponse.accessTokenExpiresIn()).isEqualTo(ACCESS_TOKEN_EXPIRE_TIME / TokenConstants.MILLI_SECOND);
    }

}
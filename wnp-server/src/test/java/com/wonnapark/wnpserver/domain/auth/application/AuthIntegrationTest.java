package com.wonnapark.wnpserver.domain.auth.application;

import com.wonnapark.wnpserver.domain.auth.config.TokenConstants;
import com.wonnapark.wnpserver.domain.auth.dto.AccessTokenResponse;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.domain.auth.dto.RefreshTokenResponse;
import com.wonnapark.wnpserver.domain.auth.exception.JwtInvalidException;
import com.wonnapark.wnpserver.domain.common.config.TestContainerConfig;
import com.wonnapark.wnpserver.global.auth.Authentication;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

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

    @Test
    @DisplayName("올바른 AccessToken 검증 테스트")
    void validateRightAccessToken() {
        // given
        AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);
        AccessTokenResponse accessTokenResponse = jwtTokenService.generateAccessToken(authTokenRequest);

        // when
        Authentication authentication = authenticationResolver.extractAuthentication(accessTokenResponse.accessToken());

        // then
        assertThatNoException().isThrownBy(() -> authenticationResolver.isValidToken(accessTokenResponse.accessToken()));
        assertThat(authTokenRequest.userId()).isEqualTo(authentication.userId());
        assertThat(authTokenRequest.age()).isEqualTo(authentication.age());
        assertThat(authTokenRequest.role()).isEqualTo(authentication.role());
    }

    @Test
    @DisplayName("잘못된 AccessToken 검증 테스트")
    void validateWrongAccessToken() {
        assertThatThrownBy(() -> authenticationResolver.isValidToken(Instancio.create(String.class)))
                .isInstanceOf(JwtInvalidException.class);
    }

    @Test
    @DisplayName("올바른 RefreshToken 검증 테스트")
    void validateRightRefreshToken() {
        // given
        AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);
        AuthTokenResponse authTokenResponse = jwtTokenService.generateAuthToken(authTokenRequest);
        String accessToken = authTokenResponse.accessToken();
        String refreshToken = authTokenResponse.refreshToken();

        // when
        Authentication authentication = authenticationResolver.extractAuthentication(accessToken);

        // then
        assertThat(authenticationResolver.isValidRefreshToken(refreshToken, authentication.userId())).isTrue();
    }

    @Nested
    @DisplayName("잘못된 RefreshToken을 검증")
    class ValidateRefreshToken {

        @Test
        @DisplayName("RefreshToken 자체가 잘못된 경우 에러를 반환")
        void validateWrongRefreshToken() {
            assertThatThrownBy(() ->
                    authenticationResolver.isValidRefreshToken(Instancio.create(String.class), Instancio.create(Long.class)))
                    .isInstanceOf(JwtInvalidException.class);
        }

        @Test
        @DisplayName("RefreshToken은 유효하지만 저장소와 일치하지 않는 경우 에러를 반환")
        void validateNotEqualRefreshToken() throws InterruptedException {
            // given
            AuthTokenRequest authTokenRequest = Instancio.create(AuthTokenRequest.class);
            AuthTokenResponse authTokenResponse = jwtTokenService.generateAuthToken(authTokenRequest);
            String accessToken = authTokenResponse.accessToken();
            String wrongToken = authTokenResponse.refreshToken();
            System.out.println("wrongToken = " + wrongToken);

            // when
            RefreshTokenResponse refreshTokenResponse = jwtTokenService.generateRefreshToken(authTokenRequest);
            System.out.println("refreshTokenResponse = " + refreshTokenResponse.refreshToken());
            Authentication authentication = authenticationResolver.extractAuthentication(accessToken);

            // then
            assertThat(authenticationResolver.isValidRefreshToken(wrongToken, authentication.userId())).isFalse();
        }
    }

}
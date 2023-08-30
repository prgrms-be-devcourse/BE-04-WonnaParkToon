package com.wonnapark.wnpserver.domain.oauth.token;

import com.wonnapark.wnpserver.domain.auth.application.JwtTokenService;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.global.common.UserInfo;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@SpringBootTest(classes = JwtTokenService.class)
class JwtTokenServiceTest {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

    @Autowired
    private JwtTokenService jwtTokenService;


    @DisplayName("인증 토큰 생성 성공")
    @Test
    void generateTokenTest() {
        //given
        AuthTokenRequest request = Instancio.of(AuthTokenRequest.class)
                .set(field(AuthTokenRequest::birthYear), "1998")
                .create();

        //when
        AuthTokenResponse authTokenResponse = jwtTokenService.generateAuthToken(request);

        //then
        assertThat(authTokenResponse.grantType()).isEqualTo("Bearer");
        assertThat(authTokenResponse.accessToken()).isNotBlank();
        assertThat(authTokenResponse.refreshToken()).isNotBlank();
        assertThat(authTokenResponse.accessTokenExpiresIn()).isNotNull();
    }

    @DisplayName("JWT Subject 검증 성공")
    @Test
    void extractUserIdTest() {
        //given
        AuthTokenRequest request = Instancio.of(AuthTokenRequest.class)
                .set(field(AuthTokenRequest::birthYear), "1998")
                .create();
        Long userID = request.userId();
        AuthTokenResponse authTokenResponse = jwtTokenService.generateAuthToken(request);
        String accessToken = authTokenResponse.accessToken();

        //when
        UserInfo userInfo = jwtTokenService.extractUserInfo(accessToken);

        //then
        assertThat(userInfo.userId()).isEqualTo(userID);
        assertThat(userInfo.birthYear()).isEqualTo(request.birthYear());
    }
}

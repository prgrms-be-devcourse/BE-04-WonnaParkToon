package com.wonnapark.wnpserver.domain.oauth.token;

import com.wonnapark.wnpserver.domain.auth.SubjectInfo;
import com.wonnapark.wnpserver.domain.auth.application.AuthTokenService;
import com.wonnapark.wnpserver.domain.auth.dto.AuthToken;
import com.wonnapark.wnpserver.domain.auth.dto.AuthTokenRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

@SpringBootTest
class AuthTokenServiceTest {

    @Autowired
    private AuthTokenService authTokenService;

    @DisplayName("JWT 토큰 생성 성공")
    @Test
    void generateTokenTest() {
        //given
        AuthTokenRequest request = Instancio.of(AuthTokenRequest.class)
                .set(field(AuthTokenRequest::birthYear), "1998")
                .create();

        //when
        AuthToken authToken = authTokenService.generate(request);

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
        AuthTokenRequest request = Instancio.of(AuthTokenRequest.class)
                .set(field(AuthTokenRequest::birthYear), "1998")
                .create();
        Long userID = request.userId();
        AuthToken authToken = authTokenService.generate(request);
        String accessToken = authToken.accessToken();

        //when
        SubjectInfo subjectInfo = authTokenService.extractSubjectInfo(accessToken);

        //then
        assertThat(subjectInfo.userID()).isEqualTo(userID);
        assertThat(subjectInfo.birthYear()).isEqualTo(request.birthYear());
    }
}
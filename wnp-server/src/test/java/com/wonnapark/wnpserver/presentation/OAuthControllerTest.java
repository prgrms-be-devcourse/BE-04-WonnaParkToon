package com.wonnapark.wnpserver.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonnapark.wnpserver.auth.application.AuthenticationResolver;
import com.wonnapark.wnpserver.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.global.auth.AuthFixtures;
import com.wonnapark.wnpserver.global.auth.AuthorizedArgumentResolver;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.oauth.OAuthProvider;
import com.wonnapark.wnpserver.oauth.application.OAuthLoginService;
import com.wonnapark.wnpserver.oauth.dto.request.KakaoLoginRequest;
import com.wonnapark.wnpserver.oauth.dto.request.NaverLoginRequest;
import com.wonnapark.wnpserver.oauth.presentation.OAuthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OAuthController.class)
class OAuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OAuthLoginService oAuthLoginService;

    @MockBean
    AuthenticationResolver authenticationResolver;

    @MockBean
    AuthorizedArgumentResolver authorizedArgumentResolver;

    @ParameterizedTest
    @EnumSource(value = OAuthProvider.class)
    @DisplayName("OAuthProvider에 따라 알맞는 AuthorizeUrl을 redirectUrl로 반환한다")
    void redirectAuthorizationRequestUrlTest(OAuthProvider oAuthProvider) throws Exception {
        // given
        String redirectUrl = "authorizeUrl";
        String pathVariable = oAuthProvider.name().toLowerCase();
        given(oAuthLoginService.getAuthorizationRequestUrl(oAuthProvider)).willReturn(redirectUrl);

        // when // then
        mockMvc.perform(get("/api/v1/oauth/{oAuthProvider}", pathVariable))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(redirectUrl))
                .andDo(print());
    }

    @Test
    @DisplayName("카카오 로그인이 진행된다.")
    void loginWithKakaoTest() throws Exception {
        // given
        AuthTokenResponse authTokenResponse = AuthFixtures.createAuthTokenResponse();
        ApiResponse<AuthTokenResponse> from = ApiResponse.from(authTokenResponse);
        given(oAuthLoginService.login(any(KakaoLoginRequest.class))).willReturn(authTokenResponse);

        // when // then
        mockMvc.perform(get("/api/v1/oauth/login/kakao")
                        .param("code", "authorizationCode"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(from)))
                .andDo(print());
    }

    @Test
    @DisplayName("네이버 로그인이 진행된다.")
    void loginWithNaverTest() throws Exception {
        // given
        AuthTokenResponse authTokenResponse = AuthFixtures.createAuthTokenResponse();
        ApiResponse<AuthTokenResponse> from = ApiResponse.from(authTokenResponse);
        given(oAuthLoginService.login(any(NaverLoginRequest.class))).willReturn(authTokenResponse);

        // when // then
        mockMvc.perform(get("/api/v1/oauth/login/naver")
                        .param("code", "authorizationCode"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(from)))
                .andDo(print());
    }


}

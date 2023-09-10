package com.wonnapark.wnpserver.auth.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonnapark.wnpserver.auth.application.AuthenticationResolver;
import com.wonnapark.wnpserver.auth.application.JwtTokenService;
import com.wonnapark.wnpserver.auth.config.TokenConstants;
import com.wonnapark.wnpserver.auth.dto.AuthTokenRequest;
import com.wonnapark.wnpserver.auth.dto.AuthTokenResponse;
import com.wonnapark.wnpserver.global.auth.AuthFixtures;
import com.wonnapark.wnpserver.global.auth.Authentication;
import com.wonnapark.wnpserver.global.auth.AuthorizedArgumentResolver;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.oauth.application.OAuthLogoutService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OAuthLogoutService oAuthLogoutService;

    @MockBean
    JwtTokenService jwtTokenService;

    @MockBean
    AuthenticationResolver authenticationResolver;

    @MockBean
    AuthorizedArgumentResolver authorizedArgumentResolver;

    @Test
    @DisplayName("인증 정보로 액세스 토큰과 리프레시 토큰을 재발급한다.")
    void reissueAuthTokenTest() throws Exception {
        // given
        Authentication authentication = AuthFixtures.createUserAuthentication();
        AuthTokenResponse authTokenResponse = AuthFixtures.createAuthTokenResponse();
        ApiResponse<AuthTokenResponse> response = ApiResponse.from(authTokenResponse);

        given(authenticationResolver.extractAuthentication(any())).willReturn(authentication);
        given(jwtTokenService.generateAuthToken(any(AuthTokenRequest.class))).willReturn(authTokenResponse);

        // when // then
        mockMvc.perform(get("/api/v1/auth/reissue")
                        .header(HttpHeaders.AUTHORIZATION, "accessToken")
                        .header(TokenConstants.REFRESH_TOKEN, "refreshToken"))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(response)))
                .andDo(print());
    }

    @Test
    @DisplayName("유저 정보와 액세스 토큰을 통해 로그아웃 처리를 진행한다.")
    void logoutTest() throws Exception {
        // given
        Authentication authentication = AuthFixtures.createUserAuthentication();
        UserInfo userInfo = UserInfo.from(authentication);

        given(authenticationResolver.extractAuthentication(any())).willReturn(authentication);
        given(authorizedArgumentResolver.supportsParameter(any())).willReturn(true);
        given(authorizedArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(userInfo);
        willDoNothing().given(oAuthLogoutService).logout(any(), any());

        // when // then
        mockMvc.perform(get("/api/v1/auth/logout")
                        .header(HttpHeaders.AUTHORIZATION, "accessToken")
                        .header(TokenConstants.REFRESH_TOKEN, "refreshToken"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}

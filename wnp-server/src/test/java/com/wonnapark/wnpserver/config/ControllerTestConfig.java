package com.wonnapark.wnpserver.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonnapark.wnpserver.auth.application.AuthenticationResolver;
import com.wonnapark.wnpserver.auth.application.JwtTokenService;
import com.wonnapark.wnpserver.episode.application.EpisodeFindUseCase;
import com.wonnapark.wnpserver.episode.application.EpisodeImageService;
import com.wonnapark.wnpserver.episode.application.EpisodeManageUseCase;
import com.wonnapark.wnpserver.global.auth.AuthorizedArgumentResolver;
import com.wonnapark.wnpserver.global.auth.JwtAuthenticationInterceptor;
import com.wonnapark.wnpserver.oauth.application.OAuthLoginService;
import com.wonnapark.wnpserver.oauth.application.OAuthLogoutService;
import com.wonnapark.wnpserver.webtoon.application.AdminWebtoonService;
import com.wonnapark.wnpserver.webtoon.application.DefaultWebtoonService;
import com.wonnapark.wnpserver.webtoon.application.GuestWebtoonService;
import com.wonnapark.wnpserver.webtoon.application.UserWebtoonService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;

@ExtendWith(RestDocumentationExtension.class)
public class ControllerTestConfig {

    protected static final String TOKEN = "Bearer header.payload.signature";

    protected ObjectMapper objectMapper = new ObjectMapper();

    // auth
    @MockBean
    protected JwtTokenService jwtTokenService;

    // oauth
    @MockBean
    protected OAuthLoginService oAuthLoginService;
    @MockBean
    protected OAuthLogoutService oAuthLogoutService;

    // global
    @MockBean
    protected AuthenticationResolver authenticationResolver;
    @MockBean
    protected AuthorizedArgumentResolver authorizedArgumentResolver;
    @MockBean
    protected JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    // episode
    @MockBean
    protected EpisodeImageService episodeImageService;
    @MockBean
    protected EpisodeManageUseCase episodeManageUseCase;
    @MockBean
    protected EpisodeFindUseCase episodeFindUseCase;

    // webtoon
    @MockBean
    protected AdminWebtoonService adminWebtoonService;
    @MockBean
    protected DefaultWebtoonService defaultWebtoonService;
    @MockBean
    protected UserWebtoonService userWebtoonService;
    @MockBean
    protected GuestWebtoonService guestWebtoonService;

}

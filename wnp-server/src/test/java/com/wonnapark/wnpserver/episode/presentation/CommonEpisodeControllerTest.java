package com.wonnapark.wnpserver.episode.presentation;

import com.wonnapark.wnpserver.auth.application.AuthenticationResolver;
import com.wonnapark.wnpserver.episode.Episode;
import com.wonnapark.wnpserver.episode.EpisodeFixtures;
import com.wonnapark.wnpserver.episode.application.EpisodeFindUseCase;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.global.auth.AuthorizedArgumentResolver;
import com.wonnapark.wnpserver.global.auth.JwtAuthenticationInterceptor;
import com.wonnapark.wnpserver.webtoon.Webtoon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommonEpisodeController.class)
@AutoConfigureRestDocs
class CommonEpisodeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EpisodeFindUseCase episodeFindUseCase;
    @MockBean
    private AuthenticationResolver authenticationResolver;
    @MockBean
    private AuthorizedArgumentResolver authorizedArgumentResolver;
    @MockBean
    private JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    @Test
    @DisplayName("에피소드 ID로 에피소드 상세 정보를 정상적으로 가져올 수 있다.")
    void findEpisodeDetailForm() throws Exception {
        // given
        Webtoon webtoon = EpisodeFixtures.createWebtoon();
        Episode episode = EpisodeFixtures.createEpisode(webtoon);
        given(episodeFindUseCase.findEpisodeDetailForm(episode.getId())).willReturn(EpisodeDetailFormResponse.from(episode));
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        // when // then
        this.mockMvc.perform(get("/api/v1/common/episode/{id}/detail", episode.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("common-episode-v1-findEpisodeDetailForm",
                        resourceDetails().tag("에피소드-공통").description("에피소드 상세 정보 불러오기"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("data.id").type(NUMBER).description("에피소드 ID"),
                                fieldWithPath("data.artistComment").type(STRING).description("작가의 말"),
                                fieldWithPath("data.title").type(STRING).description("에피소드 제목"),
                                fieldWithPath("data.episodeUrlResponses").type(ARRAY).description("에피소드 URL 리스트"),
                                fieldWithPath("data.episodeUrlResponses[].url").type(STRING).description("에피소드 URL")
                        )
                ));
    }

    @Test
    @DisplayName("웹툰 ID로 에피소드 리스트 정보를 정상적으로 가져올 수 있다.")
    void findEpisodeListForm() throws Exception {
        // given
        Pageable pageable = EpisodeFixtures.createPageable();
        Webtoon webtoon = EpisodeFixtures.createWebtoon();
        List<Episode> episodes = EpisodeFixtures.createEpisodes(webtoon);
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(episodeFindUseCase.findEpisodeListForm(webtoon.getId(), pageable))
                .willReturn(new PageImpl<>(episodes, pageable, episodes.size()).map(EpisodeListFormResponse::from));
        // when // then
        mockMvc.perform(get("/api/v1/common/episode/list")
                        .param("webtoonId", String.valueOf(webtoon.getId()))
                        .param("page", "0")
                        .param("direction", "DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("common-episode-v1-findEpisodeListForm",
                        resourceDetails().tag("에피소드-공통").description("에피소드 리스트 불러오기"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("webtoonId").description("웹툰 ID"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("direction").description("정렬 방식")
                        ),
                        responseFields(
                                fieldWithPath("data.content[].id").description("ID"),
                                fieldWithPath("data.content[].title").description("제목"),
                                fieldWithPath("data.content[].thumbnail").description("썸네일"),
                                fieldWithPath("data.content[].releaseDate").description("출시 날짜"),
                                fieldWithPath("data.content[].isViewed").description("조회 여부"),
                                fieldWithPath("data.numberOfElements").description("요소의 수"),
                                fieldWithPath("data.page").description("현재 페이지"),
                                fieldWithPath("data.size").description("페이지 크기"),
                                fieldWithPath("data.sort.empty").description("정렬 여부"),
                                fieldWithPath("data.sort.sorted").description("정렬됨"),
                                fieldWithPath("data.sort.unsorted").description("정렬되지 않음"),
                                fieldWithPath("data.totalPages").description("총 페이지 수"),
                                fieldWithPath("data.totalElements").description("총 요소 수")
                        )
                ));
    }

}

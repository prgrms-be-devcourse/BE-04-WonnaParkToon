package com.wonnapark.wnpserver.domain.episode.presentation;

import com.wonnapark.wnpserver.domain.episode.Episode;
import com.wonnapark.wnpserver.domain.episode.application.EpisodeFind;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
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
import static com.wonnapark.wnpserver.domain.episode.EpisodeFixtures.createEpisode;
import static com.wonnapark.wnpserver.domain.episode.EpisodeFixtures.createEpisodes;
import static com.wonnapark.wnpserver.domain.episode.EpisodeFixtures.createPageable;
import static com.wonnapark.wnpserver.domain.episode.EpisodeFixtures.createWebtoon;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserEpisodeController.class)
@AutoConfigureRestDocs
class UserEpisodeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EpisodeFind episodeFind;

    @Test
    @DisplayName("에피소드 ID로 에피소드 상세 정보를 정상적으로 가져올 수 있다.")
    void findEpisodeDetailForm() throws Exception {
        // given
        Long userId = 1L;
        Webtoon webtoon = createWebtoon();
        Episode episode = createEpisode(webtoon);
        System.out.println(episode.getId());
        given(episodeFind.findEpisodeDetailForm(userId, episode.getId())).willReturn(EpisodeDetailFormResponse.from(episode));
        // when // then
        this.mockMvc.perform(get("/api/v1/user/episode/detail/{episodeId}", episode.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-episode-v1-findEpisodeDetailForm",
                        resourceDetails().tag("에피소드-유저").description("에피소드 상세 정보 불러오기"),
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
    @DisplayName("유저는 웹툰 ID로 에피소드 리스트 정보를 정상적으로 가져올 수 있다.")
    void findEpisodeListForm() throws Exception {
        // given
        Long userId = 1L;
        Pageable pageable = createPageable();
        Webtoon webtoon = createWebtoon();
        List<Episode> episodes = createEpisodes(webtoon);
        given(episodeFind.findEpisodeListForm(userId, webtoon.getId(), pageable))
                .willReturn(new PageImpl<>(episodes, pageable, episodes.size()).map(EpisodeListFormResponse::from));
        // when // then
        mockMvc.perform(get("/api/v1/user/episode/{webtoonId}/list", webtoon.getId())
                        .param("page", "0")
                        .param("size", "20")
                        .param("sort", "createAt,desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user-episode-v1-findEpisodeListForm",
                        resourceDetails().tag("에피소드-유저").description("에피소드 리스트 불러오기"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("webtoonId").description("웹툰 ID")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기"),
                                parameterWithName("sort").description("정렬 방식")
                        ),
                        responseFields(
                                fieldWithPath("data.content[].id").type(NUMBER).description("에피소드 ID"),
                                fieldWithPath("data.content[].title").type(STRING).description("에피소드 제목"),
                                fieldWithPath("data.content[].thumbnail").type(STRING).description("에피소드 썸네일 URL"),
                                fieldWithPath("data.content[].releaseDate").type(STRING).description("발행 날짜"),
                                fieldWithPath("data.content[].isViewed").type(BOOLEAN).description("조회 여부"),
                                fieldWithPath("data.pageable.sort.empty").type(BOOLEAN).description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.pageable.sort.sorted").type(BOOLEAN).description("정렬이 되어있는지 여부"),
                                fieldWithPath("data.pageable.sort.unsorted").type(BOOLEAN).description("정렬이 안 되어있는지 여부"),
                                fieldWithPath("data.pageable.offset").type(NUMBER).description("페이지 시작점"),
                                fieldWithPath("data.pageable.pageNumber").type(NUMBER).description("페이지 번호"),
                                fieldWithPath("data.pageable.pageSize").type(NUMBER).description("페이지 크기"),
                                fieldWithPath("data.pageable.paged").type(BOOLEAN).description("페이징이 되어있는지 여부"),
                                fieldWithPath("data.pageable.unpaged").type(BOOLEAN).description("페이징이 안 되어있는지 여부"),
                                fieldWithPath("data.last").type(BOOLEAN).description("마지막 페이지인지 여부"),
                                fieldWithPath("data.totalElements").type(NUMBER).description("총 엘리먼트 개수"),
                                fieldWithPath("data.totalPages").type(NUMBER).description("총 페이지 수"),
                                fieldWithPath("data.first").type(BOOLEAN).description("첫 페이지인지 여부"),
                                fieldWithPath("data.size").type(NUMBER).description("페이지 크기"),
                                fieldWithPath("data.number").type(NUMBER).description("페이지 번호"),
                                fieldWithPath("data.sort.empty").type(BOOLEAN).description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.sort.sorted").type(BOOLEAN).description("정렬이 되어있는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(BOOLEAN).description("정렬이 안 되어있는지 여부"),
                                fieldWithPath("data.numberOfElements").type(NUMBER).description("현재 페이지의 엘리먼트 개수"),
                                fieldWithPath("data.empty").type(BOOLEAN).description("응답이 비어있는지 여부")
                        )
                ));
    }

}

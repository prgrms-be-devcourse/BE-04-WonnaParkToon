package com.wonnapark.wnpserver.episode.presentation;

import com.wonnapark.wnpserver.config.ControllerTestConfig;
import com.wonnapark.wnpserver.episode.dto.request.*;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminEpisodeControllerTest extends ControllerTestConfig {

    @BeforeEach
    void setup() throws Exception {
        given(jwtAuthenticationInterceptor.preHandle(any(), any(), any())).willReturn(true);
    }

    @Test
    @DisplayName("새로운 에피소드를 생성하고 Location 헤더와 에피소드 ID를 반환 할 수 있다.")
    void episodeCreate() throws Exception {
        // given
        Long webtoonId = 1L;
        EpisodeCreationRequest episodeCreationRequest = createEpisodeCreationRequest();
        Long createdEpisodeId = Instancio.create(Long.class);
        given(episodeManageUseCase.createEpisode(webtoonId, episodeCreationRequest)).willReturn(createdEpisodeId);
        // when // then
        mockMvc.perform(post("/api/v1/admin/episode")
                        .param("webtoonId", String.valueOf(webtoonId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeCreationRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("/api/v1/common/episode/detail/%d", createdEpisodeId)))
                .andDo(document("admin-episode-v1-post-createEpisode",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 생성"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("webtoonId").description("웹툰 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(STRING).description("에피소드 제목"),
                                fieldWithPath("releaseDateTime").type(STRING).description("에피소드 공개일"),
                                fieldWithPath("thumbnail").type(STRING).description("에피소드 썸네일 URL"),
                                fieldWithPath("artistComment").type(STRING).description("작가의 말"),
                                fieldWithPath("episodeUrlCreationRequests").type(ARRAY).description("에피소드 URL 리스트"),
                                fieldWithPath("episodeUrlCreationRequests[].episodeUrl").type(STRING).description("에피소드 URL")
                        ),
                        responseHeaders(headerWithName("Location").description("새로 생성된 리소스의 URI")),
                        responseFields(
                                fieldWithPath("data.id").type(NUMBER).description("에피소드 제목")
                        )
                ));
    }

    @Test
    @DisplayName("에피소드 제목을 업데이트 할 수 있다.")
    void updateEpisodeTitle() throws Exception {
        Long episodeId = Instancio.create(Long.class);
        EpisodeTitleUpdateRequest request = createEpisodeTitleUpdateRequest();

        mockMvc.perform(patch("/api/v1/admin/episode/{id}/title", episodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-patch-updateEpisodeTitle",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 제목 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(STRING).description("새로운 에피소드 제목")
                        )));
    }

    @Test
    @DisplayName("에피소드 작가의 말을 업데이트 할 수 있다.")
    void updateEpisodeArtistComment() throws Exception {
        Long episodeId = Instancio.create(Long.class);
        EpisodeArtistCommentUpdateRequest request = createEpisodeArtistCommentUpdateRequest();

        mockMvc.perform(patch("/api/v1/admin/episode/{id}/artist-comment", episodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-patch-updateEpisodeArtistComment",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 작가의 말 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        ),
                        requestFields(
                                fieldWithPath("artistComment").type(STRING).description("새로운 에피소드 작가의 말")
                        )));
    }

    @Test
    @DisplayName("에피소드 썸네일을 업데이트 할 수 있다.")
    void updateEpisodeThumbnail() throws Exception {
        Long episodeId = Instancio.create(Long.class);
        EpisodeThumbnailUpdateRequest request = createEpisodeThumbnailUpdateRequest();

        mockMvc.perform(patch("/api/v1/admin/episode/{id}/thumbnail", episodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-patch-updateEpisodeThumbnail",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 썸네일 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        ),
                        requestFields(
                                fieldWithPath("thumbnail").type(STRING).description("새로운 에피소드 썸네일")
                        )));
    }

    @Test
    @DisplayName("에피소드 공개일을 업데이트 할 수 있다.")
    void updateEpisodeReleaseDateTime() throws Exception {
        Long episodeId = Instancio.create(Long.class);
        EpisodeReleaseDateTimeUpdateRequest request = createEpisodeReleaseDateTimeUpdateRequest();

        mockMvc.perform(patch("/api/v1/admin/episode/{id}/release-datetime", episodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-patch-updateEpisodeReleaseDateTime",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 공개일 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        ),
                        requestFields(
                                fieldWithPath("releaseDateTime").type(STRING).description("새로운 에피소드 공개일")
                        )));
    }

    @Test
    @DisplayName("에피소드 URL 전부를 업데이트 할 수 있다.")
    void updateEpisodeUrls() throws Exception {
        Long episodeId = Instancio.create(Long.class);
        EpisodeUrlsUpdateRequest request = createEpisodeUrlsUpdateRequest();

        mockMvc.perform(patch("/api/v1/admin/episode/{id}/image-urls", episodeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-patch-updateEpisodeUrls",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 URL 수정"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        ),
                        requestFields(
                                fieldWithPath("urls[]").description("에피소드 이미지 URL 목록")
                        )));
    }

    @Test
    @DisplayName("에피소드를 삭제할 수 있다.")
    void deleteEpisode() throws Exception {
        Long episodeId = Instancio.create(Long.class);

        mockMvc.perform(delete("/api/v1/admin/episode/{id}", episodeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("admin-episode-v1-delete-deleteEpisode",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 삭제"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("에피소드 ID")
                        )
                ));
    }

    private EpisodeCreationRequest createEpisodeCreationRequest() {
        return Instancio.of(EpisodeCreationRequest.class)
                .generate(field(EpisodeCreationRequest::title), gen -> gen.string().length(1, 35))
                .generate(field(EpisodeCreationRequest::thumbnail), gen -> gen.string().length(1, 255))
                .generate(field(EpisodeCreationRequest::artistComment), gen -> gen.string().length(1, 100))
                .create();
    }

    private EpisodeTitleUpdateRequest createEpisodeTitleUpdateRequest() {
        return Instancio.create(EpisodeTitleUpdateRequest.class);
    }

    private EpisodeThumbnailUpdateRequest createEpisodeThumbnailUpdateRequest() {
        return Instancio.create(EpisodeThumbnailUpdateRequest.class);
    }

    private EpisodeArtistCommentUpdateRequest createEpisodeArtistCommentUpdateRequest() {
        return Instancio.create(EpisodeArtistCommentUpdateRequest.class);
    }

    private EpisodeReleaseDateTimeUpdateRequest createEpisodeReleaseDateTimeUpdateRequest() {
        return Instancio.create(EpisodeReleaseDateTimeUpdateRequest.class);
    }

    private EpisodeUrlsUpdateRequest createEpisodeUrlsUpdateRequest() {
        return Instancio.create(EpisodeUrlsUpdateRequest.class);
    }

}

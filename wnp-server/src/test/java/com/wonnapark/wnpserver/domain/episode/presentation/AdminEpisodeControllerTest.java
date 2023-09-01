package com.wonnapark.wnpserver.domain.episode.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonnapark.wnpserver.domain.episode.application.EpisodeManage;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeArtistCommentUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeCreationRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeReleaseDateTimeUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeThumbnailUpdateRequest;
import com.wonnapark.wnpserver.domain.episode.dto.request.EpisodeTitleUpdateRequest;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static com.wonnapark.wnpserver.domain.episode.EpisodeFixtures.createWebtoon;
import static org.instancio.Select.field;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminEpisodeController.class)
@AutoConfigureRestDocs
class AdminEpisodeControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    EpisodeManage episodeManage;

    @Test
    @DisplayName("새로운 에피소드를 생성하고 Location 헤더와 에피소드 ID를 반환 할 수 있다.")
    void episodeCreate() throws Exception {
        // given
        Webtoon webtoon = createWebtoon();
        EpisodeCreationRequest episodeCreationRequest = createEpisodeCreationRequest();
        Long createdEpisodeId = Instancio.create(Long.class);
        given(episodeManage.createEpisode(webtoon.getId(), episodeCreationRequest)).willReturn(createdEpisodeId);
        // when // then
        mockMvc.perform(post("/api/v1/admin/episode/{webtoonId}", webtoon.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(episodeCreationRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("/api/v1/common/episode/detail/%d", createdEpisodeId)))
                .andDo(document("admin-episode-v1-post-createEpisode",
                        resourceDetails().tag("에피소드-관리자").description("에피소드 생성"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("webtoonId").description("Webtoon ID")
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
}

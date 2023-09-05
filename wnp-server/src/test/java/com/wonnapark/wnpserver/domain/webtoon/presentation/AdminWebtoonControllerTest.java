package com.wonnapark.wnpserver.domain.webtoon.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.WebtoonFixtures;
import com.wonnapark.wnpserver.domain.webtoon.application.AdminWebtoonService;
import com.wonnapark.wnpserver.domain.webtoon.dto.request.WebtoonCreateRequest;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminWebtoonController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
class AdminWebtoonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private AdminWebtoonService adminWebtoonService;

    @Test
    @DisplayName("새로운 웹툰을 생성하고 웹툰 상세 정보를 반환할 수 있다.")
    void createWebtoon() throws Exception {
        // given
        WebtoonCreateRequest webtoonCreateRequest = WebtoonFixtures.createWebtoonCreateRequest();
        Webtoon webtoon = WebtoonCreateRequest.toEntity(webtoonCreateRequest);
        given(adminWebtoonService.createWebtoon(webtoonCreateRequest)).willReturn(WebtoonDetailResponse.from(webtoon));

        // when, then
        mockMvc.perform(post("/api/v1/admin/webtoons")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(webtoonCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("/api/v1/webtoons/%d", webtoon.getId())))
                .andDo(document("admin-webtoon-v1-post-createWebtoon",
                        resourceDetails().tag("웹툰-관리자").description("웹툰 생성"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("웹툰 제목"),
                                fieldWithPath("artist").type(JsonFieldType.STRING).description("웹툰 작가"),
                                fieldWithPath("detail").type(JsonFieldType.STRING).description("웹툰 설명"),
                                fieldWithPath("genre").type(JsonFieldType.STRING).description("웹툰 장르"),
                                fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("웹툰 썸네일"),
                                fieldWithPath("ageRating").type(JsonFieldType.STRING).description("웹툰 연령 등급"),
                                fieldWithPath("publishDays").type(JsonFieldType.ARRAY).description("웹툰 연재 요일")
                        )
                ));
    }
    
}

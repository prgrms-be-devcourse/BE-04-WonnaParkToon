package com.wonnapark.wnpserver.domain.webtoon.presentation;

import com.wonnapark.wnpserver.webtoon.Webtoon;
import com.wonnapark.wnpserver.domain.webtoon.WebtoonFixtures;
import com.wonnapark.wnpserver.webtoon.application.DefaultWebtoonService;
import com.wonnapark.wnpserver.webtoon.application.UserWebtoonService;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonSimpleResponse;
import com.wonnapark.wnpserver.webtoon.presentation.DefaultWebtoonController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DefaultWebtoonController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
class DefaultWebtoonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DefaultWebtoonService defaultWebtoonService;
    @MockBean
    private UserWebtoonService userWebtoonService;

    // TODO: 2023-09-05 findWebtoonById 테스트 코드 작성
    // TODO: 2023-09-06 User 통합 테스트 필요 

    @ParameterizedTest
    @DisplayName("연재 요일로 해당 연재 요일의 웹툰 목록을 조회할 수 있다.")
    @EnumSource(DayOfWeek.class)
    void findWebtoonsByPublishDay(DayOfWeek publishDay) throws Exception {
        // given
        List<Webtoon> webtoons = WebtoonFixtures.createWebtoons();
        given(defaultWebtoonService.findWebtoonsByPublishDay(publishDay))
                .willReturn(webtoons.stream()
                        .map(WebtoonSimpleResponse::from)
                        .toList());

        // when, then
        mockMvc.perform(get("/api/v1/webtoons/list")
                        .queryParam("publishDay", publishDay.name()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("default-webtoon-v1-findWebtoonsByPublishDay",
                        resourceDetails().tag("웹툰-기본")
                                .description("특정 요일의 웹툰 상세 정보 불러오기"),
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("publishDay").description("연재 요일")
                        ),
                        responseFields(
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("웹툰 ID"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("웹툰 제목"),
                                fieldWithPath("data[].artist").type(JsonFieldType.STRING).description("웹툰 작가"),
                                fieldWithPath("data[].thumbnail").type(JsonFieldType.STRING).description("웹툰 썸네일"),
                                fieldWithPath("data[].ageRating").type(JsonFieldType.STRING).description("웹툰 연령 등급")
                        )));

    }

}

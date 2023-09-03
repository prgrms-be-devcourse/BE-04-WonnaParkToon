package com.wonnapark.wnpserver.domain.webtoon.presentation;

import com.wonnapark.wnpserver.domain.webtoon.application.DefaultWebtoonService;
import com.wonnapark.wnpserver.domain.webtoon.application.UserWebtoonService;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonDetailResponse;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonSimpleResponse;
import com.wonnapark.wnpserver.domain.webtoon.dto.response.WebtoonsOnPublishDayResponse;
import com.wonnapark.wnpserver.global.common.UserInfo;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("api/v1/webtoons")
@RequiredArgsConstructor
public class DefaultWebtoonController {

    private final DefaultWebtoonService defaultWebtoonService;
    private final UserWebtoonService userWebtoonService;

    @GetMapping("/{webtoonId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<WebtoonDetailResponse> getWebtoonDetail(@PathVariable Long webtoonId, @Authorized UserInfo userInfo) {
        WebtoonDetailResponse response = userWebtoonService.findWebtoonById(webtoonId, userInfo);
        return ApiResponse.from(response);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<WebtoonSimpleResponse>> getWebtoonListOfPublishDay(@RequestParam DayOfWeek publishDay) {
        List<WebtoonSimpleResponse> data = defaultWebtoonService.findWebtoonsByPublishDay(publishDay);
        return ApiResponse.from(data);
    }

    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Page<WebtoonSimpleResponse>> getWeboonListWithPaging(@PageableDefault Pageable pageable) {
        Page<WebtoonSimpleResponse> data = defaultWebtoonService.findAllWebtoonsWithPaging(pageable);

        return ApiResponse.from(data);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<WebtoonsOnPublishDayResponse>> getAllWebtoonsOnEachPublishDays() {
        List<WebtoonsOnPublishDayResponse> data = defaultWebtoonService.findAllWebtoonsOnEachPublishDay();

        return ApiResponse.from(data);
    }

}

package com.wonnapark.wnpserver.webtoon.presentation;

import com.wonnapark.wnpserver.global.response.ApiResponse;
import com.wonnapark.wnpserver.webtoon.application.AdminWebtoonService;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonCreateRequest;
import com.wonnapark.wnpserver.webtoon.dto.request.WebtoonUpdateRequest;
import com.wonnapark.wnpserver.webtoon.dto.response.WebtoonDetailResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/admin/webtoons")
@RequiredArgsConstructor
public class AdminWebtoonController {

    private final AdminWebtoonService adminWebtoonService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<WebtoonDetailResponse> createWebtoon(@Valid @ModelAttribute WebtoonCreateRequest request, HttpServletResponse response) throws IOException {
        WebtoonDetailResponse data = adminWebtoonService.createWebtoon(request);
        String uri = URI.create(String.format("/api/v1/webtoons/%d", data.id())).toString();
        response.setHeader("Location", uri);

        return ApiResponse.from(data);
    }

    @PatchMapping("/{webtoonId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<WebtoonDetailResponse> updateWebtoon(@PathVariable Long webtoonId, @RequestBody WebtoonUpdateRequest request) {
        WebtoonDetailResponse response = adminWebtoonService.updateWebtoon(request, webtoonId);

        return ApiResponse.from(response);
    }

    @DeleteMapping("/{webtoonId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LocalDateTime> deleteWebtoon(@PathVariable Long webtoonId) {
        LocalDateTime deletedDateTime = adminWebtoonService.deleteWebtoon(webtoonId);

        return ApiResponse.from(deletedDateTime);
    }

}

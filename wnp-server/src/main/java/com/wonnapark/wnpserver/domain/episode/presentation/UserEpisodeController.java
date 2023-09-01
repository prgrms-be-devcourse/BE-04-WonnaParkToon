package com.wonnapark.wnpserver.domain.episode.presentation;

import com.wonnapark.wnpserver.domain.episode.application.EpisodeFind;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/user/episode")
@RequiredArgsConstructor
public class UserEpisodeController {

    private final EpisodeFind episodeFind;

    @GetMapping("/detail/{episodeId}")
    @ResponseStatus(OK)
    ApiResponse<EpisodeDetailFormResponse> findEpisodeDetailForm(@PathVariable Long episodeId) {
        Long userId = 1L; // TODO: parameter에 UserInfo 추가하고 삭제
        EpisodeDetailFormResponse episodeDetailForm = episodeFind.findEpisodeDetailForm(userId, episodeId);
        return ApiResponse.from(episodeDetailForm);
    }

    @GetMapping("/{webtoonId}/list")
    @ResponseStatus(OK)
    ApiResponse<Page<EpisodeListFormResponse>> findEpisodeListForm(
            @PathVariable Long webtoonId,
            @PageableDefault(size = 20, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long userId = 1L; // TODO: parameter에 UserInfo 추가하고 삭제
        Page<EpisodeListFormResponse> episodeListForms = episodeFind.findEpisodeListForm(userId, webtoonId, pageable);
        return ApiResponse.from(episodeListForms);
    }

}

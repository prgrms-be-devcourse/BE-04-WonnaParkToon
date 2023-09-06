package com.wonnapark.wnpserver.domain.episode.presentation;

import com.wonnapark.wnpserver.domain.episode.application.EpisodeFindUseCase;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/common/episode")
@RequiredArgsConstructor
public class CommonEpisodeController {

    private final EpisodeFindUseCase episodeFindUseCase;

    @GetMapping("/{episodeId}/detail")
    @ResponseStatus(OK)
    ApiResponse<EpisodeDetailFormResponse> findEpisodeDetailForm(@PathVariable Long episodeId) {
        EpisodeDetailFormResponse episodeDetailForm = episodeFindUseCase.findEpisodeDetailForm(episodeId);
        return ApiResponse.from(episodeDetailForm);
    }

    @GetMapping("/list")
    @ResponseStatus(OK)
    ApiResponse<Page<EpisodeListFormResponse>> findEpisodeListForm(
            @RequestParam Long webtoonId,
            @PageableDefault(size = 20, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<EpisodeListFormResponse> episodeListForms = episodeFindUseCase.findEpisodeListForm(webtoonId, pageable);
        return ApiResponse.from(episodeListForms);
    }

}

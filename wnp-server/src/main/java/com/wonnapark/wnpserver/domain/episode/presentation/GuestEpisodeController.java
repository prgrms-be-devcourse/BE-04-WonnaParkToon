package com.wonnapark.wnpserver.domain.episode.presentation;

import com.wonnapark.wnpserver.domain.episode.application.EpisodeService;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeDetailFormResponse;
import com.wonnapark.wnpserver.domain.episode.dto.response.EpisodeListFormResponse;
import com.wonnapark.wnpserver.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/guest/episode")
@RequiredArgsConstructor
public class GuestEpisodeController {

    private final EpisodeService episodeService;

    @GetMapping("/detail/{id}")
    @ResponseStatus(OK)
    public ApiResponse<EpisodeDetailFormResponse> episodeDetailFormFind(@PathVariable Long id) {
        EpisodeDetailFormResponse episodeMainForm = episodeService.findEpisodeMainForm(id);
        return ApiResponse.from(episodeMainForm);
    }

    @GetMapping("/list/{webtoonId}")
    @ResponseStatus(OK)
    public ApiResponse<Page<EpisodeListFormResponse>> episodeListFormFind(@PathVariable Long webtoonId, @PageableDefault Pageable pageable) {
        Page<EpisodeListFormResponse> episodeListForm = episodeService.findEpisodeListForm(webtoonId, pageable);
        return ApiResponse.from(episodeListForm);
    }

}

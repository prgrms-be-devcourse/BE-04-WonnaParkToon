package com.wonnapark.wnpserver.domain.episode;

import com.wonnapark.wnpserver.domain.user.User;
import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "view_historys", indexes = @Index(name = "index_user_webtoon", columnList = "user_id, webtoon_id"))
// TODO: 복합 키??, index ??, index 없이 그냥??
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webtoon_id", nullable = false)
    private Webtoon webtoon;

    @Builder
    private ViewHistory(Episode episode, User user, Webtoon webtoon) {
        this.episode = episode;
        this.user = user;
        this.webtoon = webtoon;
    }

}

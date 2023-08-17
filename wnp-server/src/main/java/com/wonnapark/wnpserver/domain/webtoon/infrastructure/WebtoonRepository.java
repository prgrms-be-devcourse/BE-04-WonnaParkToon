package com.wonnapark.wnpserver.domain.webtoon.infrastructure;

import com.wonnapark.wnpserver.domain.webtoon.Webtoon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
}

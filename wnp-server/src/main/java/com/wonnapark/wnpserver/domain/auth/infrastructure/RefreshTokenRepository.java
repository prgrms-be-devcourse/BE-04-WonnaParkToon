package com.wonnapark.wnpserver.domain.auth.infrastructure;

import com.wonnapark.wnpserver.domain.auth.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}

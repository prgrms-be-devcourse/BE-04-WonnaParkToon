package com.wonnapark.wnpserver.domain.user.infrastructure;

import com.wonnapark.wnpserver.domain.oauth.OAuthProvider;
import com.wonnapark.wnpserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderIdAndPlatform(Long providerId, OAuthProvider platform);
}

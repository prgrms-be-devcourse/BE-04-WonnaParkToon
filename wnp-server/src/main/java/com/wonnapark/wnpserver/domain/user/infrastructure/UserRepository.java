package com.wonnapark.wnpserver.domain.user.infrastructure;

import com.wonnapark.wnpserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

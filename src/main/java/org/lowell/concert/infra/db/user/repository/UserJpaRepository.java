package org.lowell.concert.infra.db.user.repository;

import org.lowell.concert.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}

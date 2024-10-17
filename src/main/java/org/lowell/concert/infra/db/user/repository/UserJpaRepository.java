package org.lowell.concert.infra.db.user.repository;

import org.lowell.concert.infra.db.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}

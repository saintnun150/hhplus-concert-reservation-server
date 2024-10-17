package org.lowell.concert.infra.db.user.repository;

import org.lowell.concert.infra.db.user.entity.UserAccountHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountHistoryJpaRepository extends JpaRepository<UserAccountHistoryEntity, Long> {
}

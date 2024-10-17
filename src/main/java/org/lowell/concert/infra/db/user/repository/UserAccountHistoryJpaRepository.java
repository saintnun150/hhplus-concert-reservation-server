package org.lowell.concert.infra.db.user.repository;

import org.lowell.concert.infra.db.user.entity.UserAccountEntity;
import org.lowell.concert.infra.db.user.entity.UserAccountHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserAccountHistoryJpaRepository extends JpaRepository<UserAccountHistoryEntity, Long> {
}

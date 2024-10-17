package org.lowell.concert.infra.db.user.repository;

import org.lowell.concert.infra.db.user.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountJpaRepository extends JpaRepository<UserAccountEntity, Long> {
    UserAccountEntity findByUserId(Long userId);
}

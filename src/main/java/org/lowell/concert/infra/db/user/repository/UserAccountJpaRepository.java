package org.lowell.concert.infra.db.user.repository;

import org.lowell.concert.infra.db.user.entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserAccountJpaRepository extends JpaRepository<UserAccountEntity, Long> {
    UserAccountEntity findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("" +
            " update UserAccountEntity ae" +
            "")
    void updateUserAccount(Long accountId, long amount);
}

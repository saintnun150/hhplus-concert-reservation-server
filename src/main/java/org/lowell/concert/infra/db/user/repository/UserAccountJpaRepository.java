package org.lowell.concert.infra.db.user.repository;

import jakarta.persistence.LockModeType;
import org.lowell.concert.domain.user.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserAccountJpaRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("" +
            " select u" +
            " from UserAccount u" +
            " where u.userId =:userId")
    Optional<UserAccount> findByUserIdWithLock(Long userId);
}

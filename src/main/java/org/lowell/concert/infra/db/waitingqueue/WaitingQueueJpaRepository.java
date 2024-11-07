package org.lowell.concert.infra.db.waitingqueue;

import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueueToken, Long> {
    Optional<WaitingQueueToken> findByToken(String token);
    Long countByTokenStatus(TokenStatus status);

    @Query("" +
            " select count(t)" +
            " from WaitingQueueToken t" +
            " where t.tokenId <= :tokenId" +
            "   and t.tokenStatus =:status")
    Long countByTokenIdLessThanEqualAndTokenStatus(Long tokenId, TokenStatus status);

    @Query("" +
            " select t" +
            " from WaitingQueueToken t" +
            " where t.tokenStatus =:status" +
            " order by t.createdAt")
    List<WaitingQueueToken> findWaitingQueuesByStatus(TokenStatus status, Pageable pageable);

    @Transactional
    @Modifying
    @Query("" +
            " update WaitingQueueToken t" +
            " set t.tokenStatus=:status, t.updatedAt=:updatedAt" +
            " where t.tokenId =:tokenId")
    void updateByTokenIdInAndTokenStatus(Long tokenId, TokenStatus status, LocalDateTime updatedAt);

    @Transactional
    @Modifying
    @Query("" +
            " update WaitingQueueToken t" +
            " set t.tokenStatus=:status, t.updatedAt=:updatedAt" +
            " where t.token =:token")
    void updateByTokenInAndTokenStatus(String token, TokenStatus status, LocalDateTime updatedAt);

    @Transactional
    @Modifying
    @Query("" +
            " update WaitingQueueToken t" +
            " set t.tokenStatus=:status, t.updatedAt=:updatedAt, t.expiresAt =:expiresAt" +
            " where t.tokenId in (:tokenIds)")
    void updateAllByTokenIdInAndTokenStatusAndExpiresAt(List<Long> tokenIds, TokenStatus status, LocalDateTime updatedAt, LocalDateTime expiresAt);
}

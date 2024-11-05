package org.lowell.concert.infra.db.waitingqueue;

import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {
    Optional<WaitingQueue> findByToken(String token);
    Long countByTokenStatus(TokenStatus status);
    Long countByTokenIdLessThanEqualAndTokenStatus(Long tokenId, TokenStatus status);

    @Query("" +
            " select t" +
            " from WaitingQueue t" +
            " where t.tokenStatus =:status" +
            " order by t.createdAt")
    List<WaitingQueue> findWaitingQueuesByStatus(TokenStatus status, Pageable pageable);

    @Transactional
    @Modifying
    @Query("" +
            " update WaitingQueue t" +
            " set t.tokenStatus=:status, t.updatedAt=:updatedAt" +
            " where t.tokenId =:tokenId")
    void updateByTokenIdInAndTokenStatus(Long tokenId, TokenStatus status, LocalDateTime updatedAt);

    @Transactional
    @Modifying
    @Query("" +
            " update WaitingQueue t" +
            " set t.tokenStatus=:status, t.updatedAt=:updatedAt, t.expiresAt =:expiresAt" +
            " where t.tokenId in (:tokenIds)")
    void updateAllByTokenIdInAndTokenStatusAndExpiresAt(List<Long> tokenIds, TokenStatus status, LocalDateTime updatedAt, LocalDateTime expiresAt);
}

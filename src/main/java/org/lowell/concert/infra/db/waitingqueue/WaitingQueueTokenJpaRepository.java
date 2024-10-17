package org.lowell.concert.infra.db.waitingqueue;

import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingQueueTokenJpaRepository extends JpaRepository<WaitingQueueTokenEntity, Long> {
    Optional<WaitingQueueTokenEntity> findByToken(String token);
    Long countByTokenStatus(TokenStatus status);
    Long countByTokenIdLessThanEqualAndTokenStatus(Long tokenId, TokenStatus status);

    @Query("" +
            " select t" +
            " from WaitingQueueTokenEntity t" +
            " where t.tokenStatus =:status" +
            " order by t.createdAt desc")
    List<WaitingQueueTokenEntity> findTokensByTokenStatusWithLimit(TokenStatus status, Pageable pageable);

    @Transactional
    @Modifying
    @Query("" +
            " update WaitingQueueTokenEntity t" +
            " set t.tokenStatus=:status, t.updatedAt=:updatedAt, t.expiresAt =:expiresAt" +
            " where t.tokenId in (:tokenIds)")
    void updateAllByTokenIdInAndTokenStatusAndExpiresAt(List<Long> tokenIds, TokenStatus status, LocalDateTime updatedAt, LocalDateTime expiresAt);
}

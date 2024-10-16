package org.lowell.concert.infra.db.waitingqueue;

import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaitingQueueTokenJpaRepository extends JpaRepository<WaitingQueueTokenEntity, Long> {
    Optional<WaitingQueueTokenEntity> findByToken(String token);
    Long countByTokenStatus(TokenStatus status);
    Long countByTokenIdLessThanEqualAndTokenStatus(Long tokenId, TokenStatus status);
}

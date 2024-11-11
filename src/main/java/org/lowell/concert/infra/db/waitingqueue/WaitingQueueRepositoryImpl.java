package org.lowell.concert.infra.db.waitingqueue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueToken;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {
    private final WaitingQueueJpaRepository jpaRepository;

    @Override
    public WaitingQueueTokenInfo createQueueToken(WaitingQueueCommand.CreateToken command) {
        WaitingQueueToken entity = WaitingQueueToken.builder()
                                                    .token(command.token())
                                                    .tokenStatus(command.status())
                                                    .createdAt(LocalDateTime.now())
                                                    .expiresAt(command.expiresAt())
                                                    .build();

        WaitingQueueToken savedEntity = jpaRepository.save(entity);
        return savedEntity.toPojo();
    }

    @Override
    public Optional<WaitingQueueTokenInfo> findQueueToken(WaitingQueueQuery.GetToken query) {
        return jpaRepository.findByToken(query.token())
                            .map(WaitingQueueToken::toPojo);
    }

    @Override
    public Long findWaitingTokenOrder(WaitingQueueQuery.GetOrder query) {
        Optional<WaitingQueueToken> queueToken = jpaRepository.findByToken(query.token());
        return queueToken.map(token -> jpaRepository.countByTokenIdLessThanEqualAndTokenStatus(token.getTokenId(),
                                                                                               token.getTokenStatus()))
                         .orElse(null);
    }

    @Override
    public Long findTokenCount(TokenStatus tokenStatus) {
        return jpaRepository.countByTokenStatus(tokenStatus);
    }

    @Override
    public List<WaitingQueueToken> findQueuesByStatusAndSize(WaitingQueueQuery.GetQueues query) {
        return jpaRepository.findWaitingQueuesByStatus(query.tokenStatus(),
                                                PageRequest.of(0, (int) query.size()));
    }

    @Override
    public void expireQueueToken(WaitingQueueCommand.ExpireToken command) {
        jpaRepository.findByToken(command.token())
                     .ifPresent(token -> jpaRepository.updateByTokenInAndTokenStatus(token.getToken(),
                                                                                     TokenStatus.EXPIRED,
                                                                                     command.now()));
    }

    @Override
    public void activateWaitingToken(WaitingQueueQuery.GetQueues query) {
        List<WaitingQueueToken> tokens = jpaRepository.findWaitingQueuesByStatus(query.tokenStatus(),
                                                                                                PageRequest.of(0, (int) query.size()));
        if (CollectionUtils.isEmpty(tokens)) {
            log.info("## No waiting queues to activate");
            return;
        }

        List<Long> tokenIds = tokens.stream()
                                    .map(WaitingQueueToken::getTokenId)
                                    .toList();
        jpaRepository.updateAllByTokenIdInAndTokenStatusAndExpiresAt(tokenIds,
                                                                     TokenStatus.ACTIVATE,
                                                                     LocalDateTime.now(),
                                                                     LocalDateTime.now().plusMinutes(ConcertPolicy.EXPIRED_QUEUE_MINUTES));
    }

    @Override
    public boolean existsActivateToken(String token) {
        return false;
    }

}

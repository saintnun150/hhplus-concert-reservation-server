package org.lowell.concert.domain.waitingqueue.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.application.waitingqueue.WaitingQueueInfo;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueError;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueToken;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingQueueService {
    private static final int ACTIVATE_CAPACITY = 50;
    private final WaitingQueueRepository queueRepository;

    @Transactional
    public WaitingQueueTokenInfo createQueueToken(WaitingQueueCommand.CreateToken command) {
        return queueRepository.createQueueToken(command);
    }

    public WaitingQueueTokenInfo getQueueToken(WaitingQueueQuery.GetToken query) {
        return queueRepository.findQueueToken(query)
                              .orElseThrow(() -> DomainException.create(WaitingQueueError.NOT_FOUND_TOKEN));
    }

    public WaitingQueueInfo.Get getQueueTokenOrder(WaitingQueueQuery.GetToken query) {
        WaitingQueueTokenInfo queueTokenInfo = getQueueToken(query);
        if (queueTokenInfo.isActivateToken(LocalDateTime.now(), ConcertPolicy.EXPIRED_QUEUE_MINUTES)) {
            return new WaitingQueueInfo.Get(queueTokenInfo.getToken(),
                                           queueTokenInfo.getTokenStatus(),
                                           queueTokenInfo.getExpiresAt(),
                                           0L);
        }
        Long order = queueRepository.findWaitingTokenOrder(new WaitingQueueQuery.GetOrder(queueTokenInfo.getToken(),
                                                                                          TokenStatus.WAITING));
        if (order == null) {
            throw DomainException.create(WaitingQueueError.NOT_WAITING_STATUS);
        }
        return new WaitingQueueInfo.Get(queueTokenInfo.getToken(),
                                        queueTokenInfo.getTokenStatus(),
                                        queueTokenInfo.getExpiresAt(),
                                        order);
    }

    public boolean hasCapacityForActiveToken() {
        Long activeCount = queueRepository.findActivateQueueTokenCount(TokenStatus.ACTIVATE);
        if (activeCount == null || activeCount == 0) {
            return true;
        }
        return activeCount < ACTIVATE_CAPACITY;
    }

    public List<WaitingQueueToken> getQueueTokensByStatusAndSize(WaitingQueueQuery.GetQueues query) {
        return queueRepository.findQueuesByStatusAndSize(query);
    }

    public int countReadyToActivateToken() {
        Long activateQueueCount = queueRepository.findActivateQueueTokenCount(TokenStatus.ACTIVATE);
        if (activateQueueCount == null || activateQueueCount == 0) {
            return ACTIVATE_CAPACITY;
        }
        return ACTIVATE_CAPACITY - activateQueueCount.intValue();
    }

    public void activateWaitingToken(WaitingQueueCommand.UpdateBatch command) {
        int count = countReadyToActivateToken();
        if (count == 0) {
            log.info("## No capacity to activate waiting queues");
            return;
        }
        List<WaitingQueueToken> waitingQueueTokens = getQueueTokensByStatusAndSize(new WaitingQueueQuery.GetQueues(TokenStatus.WAITING, count));
        if (waitingQueueTokens.isEmpty()) {
            log.info("## No waiting queues to activate");
            return;
        }
        List<Long> tokenIds = waitingQueueTokens.stream()
                                                .map(WaitingQueueToken::getTokenId)
                                                .toList();
        command = new WaitingQueueCommand.UpdateBatch(tokenIds,
                                                      TokenStatus.ACTIVATE,
                                                      command.updatedAt(),
                                                      command.expiresAt());
        queueRepository.updateAll(command);
    }

    public void ensureQueueTokenIsActiveAndValid(WaitingQueueQuery.CheckQueueActivation query) {
        WaitingQueueTokenInfo queueTokenInfo = getQueueToken(new WaitingQueueQuery.GetToken(query.token()));
        queueTokenInfo.validateTokenIsActiveAndNotExpired(query.now(), ConcertPolicy.EXPIRED_QUEUE_MINUTES);
    }

    public void expireQueueToken(WaitingQueueCommand.ExpireToken command) {
        WaitingQueueTokenInfo queueTokenInfo = getQueueToken(new WaitingQueueQuery.GetToken(command.token()));
        queueTokenInfo.validateTokenIsActiveAndNotExpired(LocalDateTime.now(), ConcertPolicy.EXPIRED_QUEUE_MINUTES);
        queueRepository.expireQueueToken(command);
    }
}

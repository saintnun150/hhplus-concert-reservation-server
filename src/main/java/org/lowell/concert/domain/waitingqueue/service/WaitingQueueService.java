package org.lowell.concert.domain.waitingqueue.service;

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
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueProvider;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WaitingQueueService {
    private final WaitingQueueRepository queueRepository;

    public WaitingQueueService(WaitingQueueProvider waitingQueueProvider, @Value("${waiting-queue.type:redis}") String type) {
        this.queueRepository = waitingQueueProvider.getWaitingQueueRepository(type);
    }

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
                                           0L, 0L);
        }
        Long order = queueRepository.findWaitingTokenOrder(new WaitingQueueQuery.GetOrder(queueTokenInfo.getToken(),
                                                                                          TokenStatus.WAITING));
        if (order == null) {
            throw DomainException.create(WaitingQueueError.NOT_WAITING_STATUS);
        }
        long waitingTime = calculateWaitingTimeSeconds(order,
                                                      ConcertPolicy.ACTIVATE_QUEUE_INTERVAL,
                                                      ConcertPolicy.ACTIVATE_QUEUE_TIME_UNIT,
                                                      ConcertPolicy.ACTIVATE_QUEUE_SIZE);


        return new WaitingQueueInfo.Get(queueTokenInfo.getToken(),
                                        queueTokenInfo.getTokenStatus(),
                                        queueTokenInfo.getExpiresAt(),
                                        order,
                                        waitingTime);
    }

    public long calculateWaitingTimeSeconds(Long order, Long intervalTime, TimeUnit unit, Long activateCount) {
        long intervalInSeconds = unit.toSeconds(intervalTime);
        long remainingIntervals = (order - 1) / activateCount;
        return remainingIntervals * intervalInSeconds;
    }

    public boolean hasCapacityForActiveToken() {
        Long activeCount = queueRepository.findTokenCount(TokenStatus.ACTIVATE);
        if (activeCount == null || activeCount == 0) {
            return true;
        }
        return activeCount < ConcertPolicy.ACTIVATE_QUEUE_CAPACITY;
    }

    public List<WaitingQueueToken> getQueueTokensByStatusAndSize(WaitingQueueQuery.GetQueues query) {
        return queueRepository.findQueuesByStatusAndSize(query);
    }

    public int countReadyToActivateToken() {
        Long activateQueueCount = queueRepository.findTokenCount(TokenStatus.ACTIVATE);
        if (activateQueueCount == null || activateQueueCount == 0) {
            return ConcertPolicy.ACTIVATE_QUEUE_CAPACITY;
        }
        return ConcertPolicy.ACTIVATE_QUEUE_CAPACITY - activateQueueCount.intValue();
    }

    public void activateWaitingToken(WaitingQueueQuery.GetQueues query) {
        queueRepository.activateWaitingToken(query);
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

package org.lowell.apps.waitingqueue.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.apps.waitingqueue.application.WaitingQueueInfo;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.concert.domain.ConcertPolicy;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueCommand;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueQuery;
import org.lowell.apps.waitingqueue.domain.exception.WaitingQueueError;
import org.lowell.apps.waitingqueue.domain.model.WaitingQueueTokenInfo;
import org.lowell.apps.waitingqueue.domain.repository.WaitingQueueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingQueueService {
    private final WaitingQueueRepository queueRepository;

    // 대기열 토큰생성
    public WaitingQueueTokenInfo createQueueToken(WaitingQueueCommand.Create command) {
        return queueRepository.createQueueToken(command);
    }

    // 대기열 토큰조회
    public WaitingQueueTokenInfo getWaitingQueueToken(WaitingQueueQuery.GetToken query) {
        return queueRepository.findWaitingQueueToken(query)
                              .orElseThrow(() -> DomainException.create(WaitingQueueError.NOT_FOUND_TOKEN));
    }

    // 참가열 토큰조회
    public WaitingQueueTokenInfo getActivateQueueToken(WaitingQueueQuery.GetToken query) {
        return queueRepository.findActivateQueueToken(query)
                              .orElseThrow(() -> DomainException.create(WaitingQueueError.NOT_FOUND_ACTIVATE_TOKEN));
    }

    // 대기열 순서 조회
    public WaitingQueueInfo.Get getQueueTokenOrder(WaitingQueueQuery.GetToken query) {
        Long order = queueRepository.findWaitingTokenOrder(query);
        if (order == null) {
            return processActivateToken(query);
        }
        return processWaitingToken(query.token(), order);
    }

    private WaitingQueueInfo.Get processActivateToken(WaitingQueueQuery.GetToken query) {
        WaitingQueueTokenInfo tokenInfo = getActivateQueueToken(query);
        tokenInfo.isExpiredTokenStatus(LocalDateTime.now());
        return WaitingQueueInfo.Get.ActivateOrder(tokenInfo.getToken(), tokenInfo.getExpiresAt());
    }

    private WaitingQueueInfo.Get processWaitingToken(String token, Long order) {
        Long waitingTime = calculateWaitingTimeSeconds(
                order,
                ConcertPolicy.ACTIVATE_QUEUE_INTERVAL,
                ConcertPolicy.ACTIVATE_QUEUE_TIME_UNIT,
                ConcertPolicy.ACTIVATE_QUEUE_SIZE
        );
        return WaitingQueueInfo.Get.WaitingOrder(token, order, waitingTime);
    }

    // 대기열 남은 시간 조회
    public long calculateWaitingTimeSeconds(Long order, Long intervalTime, TimeUnit unit, Long activateCount) {
        long intervalInSeconds = unit.toSeconds(intervalTime);
        long remainingIntervals = (order - 1) / activateCount;
        return remainingIntervals * intervalInSeconds;
    }

    // 대기열 활성화
    public void activateWaitingToken() {
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(ConcertPolicy.EXPIRED_QUEUE_MINUTES);
        queueRepository.activateWaitingToken(new WaitingQueueQuery.GetQueues(ConcertPolicy.ACTIVATE_QUEUE_SIZE, expiresAt));
    }

    // 참가열 체크
    public void checkActivateToken(WaitingQueueQuery.GetToken query) {
        WaitingQueueTokenInfo tokenInfo = getActivateQueueToken(query);
        tokenInfo.checkActivateToken(LocalDateTime.now());
    }

    // 대기열 만료 처리
    public void expireQueueToken(WaitingQueueCommand.ExpireToken command) {
        queueRepository.expireQueueToken(command);
    }
}

package org.lowell.concert.application.waitingqueue;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;
import org.lowell.concert.domain.waitingqueue.service.WaitingQueueService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WaitingQueueFacade {
    private final WaitingQueueService waitingQueueService;

    public WaitingQueueInfo.Get createWaitingQueue() {
        String token = UUID.randomUUID().toString();
        TokenStatus status = TokenStatus.WAITING;
        LocalDateTime expiresAt = null;
        boolean activationQueueImmediately = waitingQueueService.createActivationQueueImmediately();
        if (activationQueueImmediately) {
            status = TokenStatus.ACTIVATE;
            expiresAt = LocalDateTime.now().plusMinutes(10);
        }

        WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token, status, expiresAt);
        WaitingQueue waitingQueue = waitingQueueService.createWaitingQueue(command);

        return new WaitingQueueInfo.Get(waitingQueue.getTokenId(),
                                        waitingQueue.getToken(), waitingQueue.getTokenStatus(),
                                        waitingQueue.getCreatedAt(), waitingQueue.getUpdatedAt(),
                                        waitingQueue.getExpiresAt());

    }

    public WaitingQueueInfo.GetWithOrder getWaitingQueueOrder(String token) {
        WaitingQueueQuery.GetQueue query = new WaitingQueueQuery.GetQueue(token);
        WaitingQueue waitingQueue = waitingQueueService.getWaitingQueue(query);
        Long order = waitingQueueService.getWaitingQueueOrder(query);

        return new WaitingQueueInfo.GetWithOrder(new WaitingQueueInfo.Get(waitingQueue.getTokenId(),
                                                                         waitingQueue.getToken(), waitingQueue.getTokenStatus(),
                                                                         waitingQueue.getCreatedAt(), waitingQueue.getUpdatedAt(),
                                                                         waitingQueue.getExpiresAt()), order);
    }

    @Transactional
    public void activateReadyWaitingQueues() {
        LocalDateTime now = LocalDateTime.now();
        waitingQueueService.activateReadyWaitingQueues(
                new WaitingQueueCommand.UpdateBatch(null,
                                                    TokenStatus.ACTIVATE,
                                                    now,
                                                    now.plusMinutes(ConcertPolicy.EXPIRED_QUEUE_MINUTES)));
    }
}

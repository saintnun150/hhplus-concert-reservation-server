package org.lowell.concert.application.waitingqueue;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
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
        boolean activationQueueImmediately = waitingQueueService.hasCapacityForActiveToken();
        if (activationQueueImmediately) {
            status = TokenStatus.ACTIVATE;
            expiresAt = LocalDateTime.now().plusMinutes(10);
        }

        WaitingQueueCommand.CreateToken command = new WaitingQueueCommand.CreateToken(token, status, expiresAt);
        WaitingQueueTokenInfo queueTokenInfo = waitingQueueService.createQueueToken(command);

        return new WaitingQueueInfo.Get(queueTokenInfo.getToken(),
                                        queueTokenInfo.getTokenStatus(),
                                        queueTokenInfo.getExpiresAt(),
                                        queueTokenInfo.getTokenStatus() == TokenStatus.ACTIVATE ? 0L : null,
                                        queueTokenInfo.getTokenStatus() == TokenStatus.ACTIVATE ? 0L : null);
    }

    public WaitingQueueInfo.Get getWaitingQueueOrder(String token) {
        return waitingQueueService.getQueueTokenOrder(new WaitingQueueQuery.GetToken(token));
    }

    @Transactional
    public void activateReadyWaitingQueues(WaitingQueueQuery.GetQueues query) {
        waitingQueueService.activateWaitingToken(query);
    }

    public void ensureQueueTokenIsActiveAndValid(String token, LocalDateTime now) {
        waitingQueueService.ensureQueueTokenIsActiveAndValid(new WaitingQueueQuery.CheckQueueActivation(token, now));
    }
}

package org.lowell.apps.waitingqueue.application;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueCommand;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueQuery;
import org.lowell.apps.waitingqueue.domain.model.TokenStatus;
import org.lowell.apps.waitingqueue.domain.model.WaitingQueueTokenInfo;
import org.lowell.apps.waitingqueue.domain.service.WaitingQueueService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WaitingQueueFacade {
    private final WaitingQueueService waitingQueueService;

    public WaitingQueueInfo.Get createWaitingQueue() {
        String token = UUID.randomUUID().toString();
        WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token);
        WaitingQueueTokenInfo tokenInfo = waitingQueueService.createQueueToken(command);
        return new WaitingQueueInfo.Get(tokenInfo.getToken(),
                                        tokenInfo.getTokenStatus(),
                                        tokenInfo.getExpiresAt(),
                                        tokenInfo.getTokenStatus() == TokenStatus.ACTIVATE ? 0L : null,
                                        tokenInfo.getTokenStatus() == TokenStatus.ACTIVATE ? 0L : null);
    }

    public WaitingQueueInfo.Get getWaitingQueueOrder(String token) {
        return waitingQueueService.getQueueTokenOrder(new WaitingQueueQuery.GetToken(token));
    }

    @Transactional
    public void activateReadyWaitingQueues() {
        waitingQueueService.activateWaitingToken();
    }

    public void checkActivateToken(String token) {
        waitingQueueService.checkActivateToken(new WaitingQueueQuery.GetToken(token));
    }
}

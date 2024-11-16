package org.lowell.concert.domain.waitingqueue.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.service.WaitingQueueService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingQueueEventListener {
    private final WaitingQueueService waitingQueueService;

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void onExpiredToken(WaitingQueueEvent.ExpireTokenEvent event) {
        log.warn("## onExpiredToken[{}]", event.getToken());
        WaitingQueueCommand.ExpireToken command = new WaitingQueueCommand.ExpireToken(event.getToken(), event.getTimestamp());
        waitingQueueService.expireQueueToken(command);
    }
}

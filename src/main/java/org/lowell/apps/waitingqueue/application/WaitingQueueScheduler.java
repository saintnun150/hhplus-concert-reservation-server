package org.lowell.apps.waitingqueue.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.common.exception.ErrorCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingQueueScheduler {
    private final WaitingQueueFacade waitingQueueFacade;

    @Scheduled(fixedRate = 1000 * 60)
    public void activateReadyTokens() {
        try {
            waitingQueueFacade.activateReadyWaitingQueues();
        } catch (Exception e) {
            log.error("## Error occurred while activating ready tokens", e);
            throw DomainException.create(ErrorCode.INTERNAL_SERVER);
        }
    }
}

package org.lowell.concert.application.waitingqueue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.application.common.exception.ApplicationError;
import org.lowell.concert.application.common.exception.ApplicationException;
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
            throw ApplicationException.create(ApplicationError.INTERNAL_SERVER);
        }
    }
}

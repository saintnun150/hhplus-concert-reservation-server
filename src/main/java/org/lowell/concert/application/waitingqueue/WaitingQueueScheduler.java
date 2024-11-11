package org.lowell.concert.application.waitingqueue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.application.common.exception.ApplicationError;
import org.lowell.concert.application.common.exception.ApplicationException;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingQueueScheduler {
    private final WaitingQueueFacade waitingQueueFacade;

    @Scheduled(fixedRate = 1000 * 60)
    public void activateReadyTokens() {
        try {
            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(ConcertPolicy.EXPIRED_QUEUE_MINUTES);
            waitingQueueFacade.activateReadyWaitingQueues(new WaitingQueueQuery.GetQueues(TokenStatus.WAITING,
                                                                                          expiresAt,
                                                                                          ConcertPolicy.ACTIVATE_QUEUE_SIZE));
        } catch (Exception e) {
            log.error("## Error occurred while activating ready tokens", e);
            throw ApplicationException.create(ApplicationError.INTERNAL_SERVER);
        }
    }
}

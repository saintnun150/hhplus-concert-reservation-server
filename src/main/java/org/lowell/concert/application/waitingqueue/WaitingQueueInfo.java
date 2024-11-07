package org.lowell.concert.application.waitingqueue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;

import java.time.LocalDateTime;

public class WaitingQueueInfo {

    @Getter
    @AllArgsConstructor
    public static class Get {
        private final String token;
        private final TokenStatus tokenStatus;
        private final LocalDateTime expiresAt;
        private final Long order;
        private final Long waitingTime;
    }

}

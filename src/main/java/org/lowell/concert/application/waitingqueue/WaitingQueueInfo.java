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

        public static Get WaitingOrder(String token, Long order, Long waitingTime) {
            return new Get(token, TokenStatus.WAITING, null, order, waitingTime);
        }

        public static Get ActivateOrder(String token, LocalDateTime expiresAt) {
            return new Get(token, TokenStatus.ACTIVATE, expiresAt, 0L, 0L);
        }

        public static Get of(String token, TokenStatus status, LocalDateTime expiresAt, Long order, Long waitingTime) {
            return new Get(token, status, expiresAt, order, waitingTime);
        }
    }

}

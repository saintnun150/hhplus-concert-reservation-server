package org.lowell.concert.interfaces.api.waitingqueue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;

import java.time.LocalDateTime;

public class WaitingQueueResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TokenInfo {
        private final String token;
        private final Long order;
    }
}

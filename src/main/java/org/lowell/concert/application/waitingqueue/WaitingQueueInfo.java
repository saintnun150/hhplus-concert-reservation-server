package org.lowell.concert.application.waitingqueue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;

import java.time.LocalDateTime;

public class WaitingQueueInfo {

    @Getter
    @RequiredArgsConstructor
    public static class Get {
        private final Long tokenId;
        private final String token;
        private final TokenStatus tokenStatus;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;
        private final LocalDateTime expiresAt;
    }

    @Getter
    @RequiredArgsConstructor
    public static class GetWithOrder {
        private final Get waitingQueue;
        private final Long order;
    }


}

package org.lowell.concert.domain.waitingqueue.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public abstract class WaitingQueueEvent {
    @Getter
    private final LocalDateTime timestamp = LocalDateTime.now();

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class ExpireTokenEvent extends WaitingQueueEvent {
        private final String token;
    }
}

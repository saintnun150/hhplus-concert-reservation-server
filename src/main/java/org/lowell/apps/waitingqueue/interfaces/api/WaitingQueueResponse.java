package org.lowell.apps.waitingqueue.interfaces.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class WaitingQueueResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TokenInfo {
        private final String token;
        private final Long order;
    }
}

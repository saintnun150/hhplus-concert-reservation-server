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
    public static class QueueInfo {
        private final Long tokenId;
        private final String token;
        private final TokenStatus waitingQueueStatus;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    public static class QueueOrderInfo {
        private final Long tokenId;
        private final int remainQueueCount;
        private final TokenStatus waitingQueueStatus;
        private final LocalDateTime expiredDate;
    }



}

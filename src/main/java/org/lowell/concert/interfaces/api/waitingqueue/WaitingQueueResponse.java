package org.lowell.concert.interfaces.api.waitingqueue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class WaitingQueueResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreatedQueueInfo {
        private final Long tokenId;
        private final String token;
        private final String waitingQueueStatus;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    public static class CurrentQueueInfo {
        private final Long tokenId;
        private final int remainQueueCount;
        private final String waitingQueueStatus;
        private final LocalDateTime expiredDate;
    }



}

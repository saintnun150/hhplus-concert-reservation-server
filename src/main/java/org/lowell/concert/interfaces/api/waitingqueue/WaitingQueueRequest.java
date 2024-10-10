package org.lowell.concert.interfaces.api.waitingqueue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WaitingQueueRequest {

    @Getter
    @NoArgsConstructor
    public static class CreateQueue {
        @Schema(description = "사용자 ID")
        private Long userId;
    }
}

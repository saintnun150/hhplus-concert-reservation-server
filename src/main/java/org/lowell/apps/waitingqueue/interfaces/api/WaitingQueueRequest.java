package org.lowell.apps.waitingqueue.interfaces.api;

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

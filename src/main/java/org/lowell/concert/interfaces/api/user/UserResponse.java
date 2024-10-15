package org.lowell.concert.interfaces.api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class UserResponse {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Info {
        @Schema(description = "사용자 id")
        private final Long userId;
        @Schema(description = "계좌 id")
        private final Long accountId;
        @Schema(description = "계좌 잔액")
        private final Long balance;
        @Schema(description = "계좌 생성일")
        private final LocalDateTime createdAt;
        @Schema(description = "계좌 갱신일")
        private final LocalDateTime updatedAt;
    }
}

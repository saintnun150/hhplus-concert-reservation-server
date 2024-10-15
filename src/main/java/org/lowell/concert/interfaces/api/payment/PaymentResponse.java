package org.lowell.concert.interfaces.api.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class PaymentResponse {

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Info {
        @Schema(description = "결제 Id")
        private final Long paymentId;

        @Schema(description = "콘서트 Id")
        private final Long concertId;

        @Schema(description = "콘서트 이름")
        private final String concertName;

        @Schema(description = "좌석 정보")
        private final Long seatNo;

        @Schema(description = "결제 금액")
        private final Long price;

        @Schema(description = "결제 완료 시간")
        private final LocalDateTime completedAt;

    }
}

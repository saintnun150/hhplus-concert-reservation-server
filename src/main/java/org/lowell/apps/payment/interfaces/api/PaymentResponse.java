package org.lowell.apps.payment.interfaces.api;

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
        @Schema(description = "예약 Id")
        private final Long reservationId;
        @Schema(description = "결제 금액")
        private final Long payAmount;
        @Schema(description = "결제 완료 시간")
        private final LocalDateTime createdAt;

    }
}

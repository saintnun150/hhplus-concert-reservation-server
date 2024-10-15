package org.lowell.concert.interfaces.api.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class PaymentRequest {

    @Data
    @NoArgsConstructor
    public static class Create {
        @Schema(description = "콘서트 예약 Id")
        private Long reservationId;
    }
}

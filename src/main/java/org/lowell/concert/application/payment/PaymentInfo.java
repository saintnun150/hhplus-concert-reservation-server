package org.lowell.concert.application.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class PaymentInfo {

    @Getter
    @RequiredArgsConstructor
    public static class Info {
        private final Long paymentId;
        private final Long reservationId;
        private final Long payAmount;
        private final LocalDateTime createdAt;
    }
}

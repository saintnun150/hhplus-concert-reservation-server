package org.lowell.apps.payment.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
public abstract class PaymentEvent {
    private final LocalDateTime timestamp = LocalDateTime.now();

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class CompletedPayment extends PaymentEvent {
        private final String eventId;
        private final String token;
    }
}

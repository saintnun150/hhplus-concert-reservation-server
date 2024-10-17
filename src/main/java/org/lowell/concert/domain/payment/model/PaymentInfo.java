package org.lowell.concert.domain.payment.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentInfo {
    private Long paymentId;
    private Long reservationId;
    private PaymentStatus status;
    private Long paymentAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

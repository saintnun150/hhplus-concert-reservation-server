package org.lowell.concert.domain.payment.dto;

import org.lowell.concert.domain.payment.model.PaymentStatus;

public class PaymentCommand {
    public record Create(Long reservationId, Long payAmount, PaymentStatus status) { }
    public record Update(Long reservationId, PaymentStatus status) {}
}

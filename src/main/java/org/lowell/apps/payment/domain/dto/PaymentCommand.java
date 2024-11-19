package org.lowell.apps.payment.domain.dto;

import org.lowell.apps.payment.domain.model.PaymentStatus;

public class PaymentCommand {
    public record Create(Long reservationId, Long payAmount, PaymentStatus status) { }
    public record Update(Long reservationId, PaymentStatus status) {}
}

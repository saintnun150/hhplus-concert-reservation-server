package org.lowell.concert.domain.payment.repository;

import org.lowell.concert.domain.payment.dto.PaymentCommand;
import org.lowell.concert.domain.payment.model.Payment;

import java.util.Optional;

public interface PaymentRepository {
    Payment createPayment(PaymentCommand.Create command);
    Optional<Payment> getPayment(Long paymentId);
    void deleteAll();
}

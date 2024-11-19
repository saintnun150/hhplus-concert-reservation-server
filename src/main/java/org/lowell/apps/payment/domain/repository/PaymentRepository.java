package org.lowell.apps.payment.domain.repository;

import org.lowell.apps.payment.domain.dto.PaymentCommand;
import org.lowell.apps.payment.domain.model.Payment;

import java.util.Optional;

public interface PaymentRepository {
    Payment createPayment(PaymentCommand.Create command);
    Optional<Payment> getPayment(Long paymentId);
    void deleteAll();
}

package org.lowell.concert.domain.payment.repository;

import org.lowell.concert.domain.payment.dto.PaymentCommand;
import org.lowell.concert.domain.payment.model.PaymentInfo;

public interface PaymentRepository {
    void createPayment(PaymentCommand.Create command);
    PaymentInfo getPayment(Long paymentId);
}

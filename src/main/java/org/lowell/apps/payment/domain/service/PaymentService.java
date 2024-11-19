package org.lowell.apps.payment.domain.service;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.payment.domain.dto.PaymentCommand;
import org.lowell.apps.payment.domain.exception.PaymentError;
import org.lowell.apps.payment.domain.model.Payment;
import org.lowell.apps.payment.domain.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment createPayment(PaymentCommand.Create command) {
        return paymentRepository.createPayment(command);
    }

    public Payment getPayment(Long paymentId) {
        return paymentRepository.getPayment(paymentId)
                                .orElseThrow(() -> DomainException.create(PaymentError.NOT_FOUND_PAYMENT, DomainException.createPayload(paymentId)));
    }

    public void deleteAll() {
        paymentRepository.deleteAll();
    }
}

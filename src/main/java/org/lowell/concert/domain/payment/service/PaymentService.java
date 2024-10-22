package org.lowell.concert.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.payment.dto.PaymentCommand;
import org.lowell.concert.domain.payment.exception.PaymentErrorCode;
import org.lowell.concert.domain.payment.model.Payment;
import org.lowell.concert.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.PanelUI;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment createPayment(PaymentCommand.Create command) {
        return paymentRepository.createPayment(command);
    }

    public Payment getPayment(Long paymentId) {
        Payment payment = paymentRepository.getPayment(paymentId)
                                           .orElseThrow(() -> new DomainException(PaymentErrorCode.NOT_FOUND_PAYMENT));
        return payment;
    }

    public void deleteAll() {
        paymentRepository.deleteAll();
    }
}

package org.lowell.concert.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.payment.dto.PaymentCommand;
import org.lowell.concert.domain.payment.exception.PaymentErrorCode;
import org.lowell.concert.domain.payment.exception.PaymentException;
import org.lowell.concert.domain.payment.model.PaymentInfo;
import org.lowell.concert.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public void createPayment(PaymentCommand.Create command) {
        if (command.reservationId() == null) {
            throw new PaymentException(PaymentErrorCode.INVALID_RESERVATION_ID);
        }
        paymentRepository.createPayment(command);
    }

    public void getPayment(Long paymentId) {
        PaymentInfo payment = paymentRepository.getPayment(paymentId);
        if (payment == null) {
            throw new PaymentException(PaymentErrorCode.NOT_FOUND_PAYMENT);
        }
    }
}

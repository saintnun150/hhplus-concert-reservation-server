package org.lowell.apps.payment.infra.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.payment.domain.dto.PaymentCommand;
import org.lowell.apps.payment.domain.model.Payment;
import org.lowell.apps.payment.domain.repository.PaymentRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository jpaRepository;

    @Transactional
    @Override
    public Payment createPayment(PaymentCommand.Create command) {
        Payment entity = Payment.builder()
                                .reservationId(command.reservationId())
                                .payAmount(command.payAmount())
                                .status(command.status())
                                .createdAt(LocalDateTime.now())
                                .build();
        return jpaRepository.save(entity);
    }

    @Override
    public Optional<Payment> getPayment(Long paymentId) {
        return jpaRepository.findById(paymentId);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}

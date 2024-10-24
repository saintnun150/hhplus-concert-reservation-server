package org.lowell.concert.infra.db.payment.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.payment.PaymentMapper;
import org.lowell.concert.domain.payment.dto.PaymentCommand;
import org.lowell.concert.domain.payment.model.PaymentInfo;
import org.lowell.concert.domain.payment.repository.PaymentRepository;
import org.lowell.concert.infra.db.payment.entity.PaymentEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository jpaRepository;
    private final PaymentMapper mapper;

    @Transactional
    @Override
    public void createPayment(PaymentCommand.Create command) {
        PaymentEntity entity = PaymentEntity.builder()
                                            .reservationId(command.reservationId())
                                            .payAmount(command.payAmount())
                                            .status(command.status())
                                            .createdAt(LocalDateTime.now())
                                            .build();
        jpaRepository.save(entity);
    }

    @Override
    public PaymentInfo getPayment(Long paymentId) {
        PaymentEntity entity = jpaRepository.findById(paymentId)
                                            .orElse(null);
        return mapper.toPojo(entity);
    }
}

package org.lowell.apps.payment.infra.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.payment.domain.dto.PaymentOutBoxQuery;
import org.lowell.apps.payment.domain.model.PaymentOutBox;
import org.lowell.apps.payment.domain.repository.PaymentOutBoxRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentOutBoxRepositoryImpl implements PaymentOutBoxRepository {
    private final PaymentOutBoxJpaRepository paymentOutBoxJpaRepository;

    @Override
    public void save(PaymentOutBox paymentOutBox) {
        paymentOutBoxJpaRepository.save(paymentOutBox);
    }

    @Override
    public PaymentOutBox getPaymentOutBox(PaymentOutBoxQuery.Get query) {
        return paymentOutBoxJpaRepository.findByEventId(query.eventId());
    }

    @Override
    public List<PaymentOutBox> getPaymentOutBoxList(PaymentOutBoxQuery.GetList query) {
        return paymentOutBoxJpaRepository.findAllByEventStatusAndCreatedAtBefore(query.status(), query.targetDate());
    }
}

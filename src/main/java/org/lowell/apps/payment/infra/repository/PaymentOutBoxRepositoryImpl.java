package org.lowell.apps.payment.infra.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.payment.domain.dto.PaymentOutBoxQuery;
import org.lowell.apps.payment.domain.model.PaymentOutBox;
import org.lowell.apps.payment.domain.repository.PaymentOutBoxRepository;
import org.springframework.stereotype.Repository;


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
}

package org.lowell.apps.payment.infra.repository;

import org.lowell.apps.payment.domain.model.PaymentOutBox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOutBoxJpaRepository extends JpaRepository<PaymentOutBox, Long> {
    PaymentOutBox findByEventId(String eventId);
}

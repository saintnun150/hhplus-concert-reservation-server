package org.lowell.apps.payment.infra.repository;

import org.lowell.apps.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}

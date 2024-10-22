package org.lowell.concert.infra.db.payment.repository;

import org.lowell.concert.domain.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}

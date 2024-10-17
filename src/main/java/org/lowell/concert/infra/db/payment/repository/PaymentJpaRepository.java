package org.lowell.concert.infra.db.payment.repository;

import org.lowell.concert.infra.db.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}

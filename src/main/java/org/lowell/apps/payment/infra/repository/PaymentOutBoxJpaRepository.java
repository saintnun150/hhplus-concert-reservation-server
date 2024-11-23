package org.lowell.apps.payment.infra.repository;

import org.lowell.apps.payment.domain.model.EventStatus;
import org.lowell.apps.payment.domain.model.PaymentOutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentOutBoxJpaRepository extends JpaRepository<PaymentOutBox, Long> {
    PaymentOutBox findByEventId(String eventId);

    @Query("" +
            " SELECT p" +
            " FROM PaymentOutBox p" +
            " WHERE p.eventStatus = :status" +
            " AND p.createdAt < :targetDate")
    List<PaymentOutBox> findAllByEventStatusAndCreatedAtBefore(EventStatus status, LocalDateTime targetDate);
}

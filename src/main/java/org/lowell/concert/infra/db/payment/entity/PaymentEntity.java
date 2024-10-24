package org.lowell.concert.infra.db.payment.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lowell.concert.domain.payment.model.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_payment")
@Getter
@NoArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "amount")
    private Long payAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public PaymentEntity(Long paymentId, Long reservationId, Long payAmount, PaymentStatus status, LocalDateTime createdAt) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.payAmount = payAmount;
        this.status = status;
        this.createdAt = createdAt;
    }
}

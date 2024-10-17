package org.lowell.concert.application.payment;

import org.lowell.concert.domain.payment.model.PaymentInfo;
import org.lowell.concert.infra.db.payment.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentInfo toPojo(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }
        return PaymentInfo.builder()
                          .paymentId(entity.getPaymentId())
                          .reservationId(entity.getReservationId())
                          .status(entity.getStatus())
                          .createdAt(entity.getCreatedAt())
                          .updatedAt(entity.getUpdatedAt())
                          .build();
    }
}

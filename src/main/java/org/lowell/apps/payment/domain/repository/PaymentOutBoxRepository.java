package org.lowell.apps.payment.domain.repository;

import org.lowell.apps.payment.domain.dto.PaymentOutBoxQuery;
import org.lowell.apps.payment.domain.model.PaymentOutBox;

public interface PaymentOutBoxRepository {
    void save(PaymentOutBox paymentOutBox);

    PaymentOutBox getPaymentOutBox(PaymentOutBoxQuery.Get query);
}

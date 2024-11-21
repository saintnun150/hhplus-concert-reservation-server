package org.lowell.apps.payment.domain.repository;

import org.lowell.apps.payment.domain.dto.PaymentOutBoxQuery;
import org.lowell.apps.payment.domain.model.PaymentOutBox;

import java.util.List;

public interface PaymentOutBoxRepository {
    void save(PaymentOutBox paymentOutBox);
    PaymentOutBox getPaymentOutBox(PaymentOutBoxQuery.Get query);
    List<PaymentOutBox> getPaymentOutBoxList(PaymentOutBoxQuery.GetList query);
}

package org.lowell.apps.payment.domain.event;

public interface PaymentEventProducer {
    void sendMessage(PaymentEvent.CompletedPayment event);
}

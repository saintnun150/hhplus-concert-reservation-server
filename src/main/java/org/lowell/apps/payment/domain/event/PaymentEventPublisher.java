package org.lowell.apps.payment.domain.event;

public interface PaymentEventPublisher {
    void publish(Object event);
}

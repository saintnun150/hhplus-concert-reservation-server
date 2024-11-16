package org.lowell.concert.domain.payment.event;

public interface PaymentEventPublisher {
    void publish(Object event);
}

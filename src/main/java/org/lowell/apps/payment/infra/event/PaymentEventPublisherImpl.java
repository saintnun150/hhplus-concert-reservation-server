package org.lowell.apps.payment.infra.event;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.payment.domain.event.PaymentEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisherImpl implements PaymentEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(Object event) {
        eventPublisher.publishEvent(event);
    }
}

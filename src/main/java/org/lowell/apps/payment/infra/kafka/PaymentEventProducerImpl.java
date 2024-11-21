package org.lowell.apps.payment.infra.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.apps.payment.domain.event.PaymentEvent;
import org.lowell.apps.payment.domain.event.PaymentEventProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventProducerImpl implements PaymentEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.template.topics.payment-complete:payment-complete-event}")
    private String paymentCompleteTopic;

    @Override
    public void sendMessage(PaymentEvent.CompletedPayment event) {
        kafkaTemplate.send(paymentCompleteTopic, event);
    }
}

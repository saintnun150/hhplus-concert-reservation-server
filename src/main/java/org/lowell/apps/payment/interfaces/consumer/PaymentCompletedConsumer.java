package org.lowell.apps.payment.interfaces.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.lowell.apps.payment.domain.dto.PaymentOutBoxQuery;
import org.lowell.apps.payment.domain.event.PaymentEvent;
import org.lowell.apps.payment.domain.service.PaymentOutBoxService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCompletedConsumer {
    private final PaymentOutBoxService paymentOutBoxService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.template.topics.payment-complete:payment-complete-event}",
            groupId = "${kafka.template.consumer.groups.payment:payment-group}"
    )
    public void updatePaymentOutBoxStatus(ConsumerRecord<String, byte[]> message, Acknowledgment ack) {
        try {
            PaymentEvent.CompletedPayment event = objectMapper.readValue(message.value(), PaymentEvent.CompletedPayment.class);
            PaymentOutBoxQuery.Get query = new PaymentOutBoxQuery.Get(event.getEventId());
            paymentOutBoxService.successPaymentOutBox(query);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("## Error occurred during payment outbox status update -> message:[{}]", message, e);
        }
    }
}

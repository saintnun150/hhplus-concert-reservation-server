package org.lowell.apps.waitingqueue.interfaces.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.lowell.apps.payment.domain.event.PaymentEvent;
import org.lowell.apps.waitingqueue.application.WaitingQueueFacade;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingQueuePaymentCompletedConsumer {
    private final WaitingQueueFacade waitingQueueFacade;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.template.topics.payment-complete:payment-complete-event}",
            groupId = "${kafka.template.consumer.groups.waiting-quque:waiting-queue-group}"
    )
    public void processTokenExpiration(ConsumerRecord<String, byte[]> message, Acknowledgment ack) {
        try {
            PaymentEvent.CompletedPayment event = objectMapper.readValue(message.value(), PaymentEvent.CompletedPayment.class);
            waitingQueueFacade.expireQueueToken(event.getToken());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("## Error occurred during token expiration -> message:[{}]", message, e);
        }
    }
}

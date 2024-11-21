package org.lowell.apps.kafka.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class KafkaTestConsumer {
    private final List<Map<String, Object>> messages = new ArrayList<>();
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topics.test:test-topic}", groupId = "kafka-test-group")
    public void consume(ConsumerRecord<String, byte[]> message, Acknowledgment ack) {
        try {
            Map map = objectMapper.readValue(message.value(), Map.class);
            messages.add(map);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error while consuming message", e);
        }

    }
}

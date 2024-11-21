package org.lowell.apps.infra;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.inferfaces.KafkaTestConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest
@EmbeddedKafka(partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"},
        ports = 9092)
public class KafkaOperatingTest {

    @Autowired
    private KafkaTestProducer kafkaTestProducer;

    @Autowired
    private KafkaTestConsumer kafkaTestConsumer;

    @Value("${kafka.topics.test:test-topic}")
    private String topic;

    @DisplayName("Kafka에 메시지를 전송했을 때, 전송한 수와 받은 수가 동일하고 메시지 내용이 일치하는지 확인한다.")
    @Test
    public void verifyProducedAndConsumedMessagesSizeAndValueEqual() {
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> message = new HashMap<>();
            message.put("message", "Hello, Kafka!-" + i);
            kafkaTestProducer.sendMessage(topic, message);
        }

        await().pollDelay(7, SECONDS)
                .untilAsserted(() -> {
                    List<Map<String, Object>> messages = kafkaTestConsumer.getMessages();
                    assertThat(messages.size()).isEqualTo(10);

                    for (int i = 0; i < 10; i++) {
                        assertThat(messages.get(i).get("message")).isEqualTo("Hello, Kafka!-" + i);
                    }
                });
    }

}

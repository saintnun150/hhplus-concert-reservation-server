package org.lowell.apps.payment.domain.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.common.util.UUIDGenerator;
import org.lowell.apps.payment.domain.dto.PaymentOutBoxQuery;
import org.lowell.apps.payment.domain.model.EventStatus;
import org.lowell.apps.payment.domain.model.EventType;
import org.lowell.apps.payment.domain.model.PaymentOutBox;
import org.lowell.apps.payment.domain.service.PaymentOutBoxService;
import org.lowell.apps.payment.infra.repository.PaymentOutBoxJpaRepository;
import org.lowell.apps.support.DatabaseCleanUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentOutBoxServiceTest {

    @Autowired
    private PaymentOutBoxJpaRepository paymentOutBoxJpaRepository;

    @Autowired
    private PaymentOutBoxService paymentOutBoxService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("paymentOutBox success를 호출하면 eventStatus와 updatedAt이 업데이트 된다.")
    @Test
    void success_shouldUpdateEventStatusAndUpdatedAt() {
        // given
        EventType eventType = EventType.PAYMENT_COMPLETED;
        String eventId = UUIDGenerator.generateTimestampUUID();

        PaymentOutBox paymentOutBox = PaymentOutBox.create(eventId, eventType, null);
        paymentOutBoxJpaRepository.save(paymentOutBox);

        PaymentOutBoxQuery.Get query = new PaymentOutBoxQuery.Get(eventId);

        // when
        paymentOutBoxService.successPaymentOutBox(query);

        // then
        PaymentOutBox updatedOutBox = paymentOutBoxJpaRepository.findById(paymentOutBox.getId()).orElseThrow();
        assertThat(updatedOutBox.getEventStatus()).isEqualTo(EventStatus.SUCCESS);
        assertThat(updatedOutBox.getUpdatedAt()).isNotNull();
    }

}
package org.lowell.apps.payment.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.apps.payment.domain.dto.PaymentOutBoxQuery;
import org.lowell.apps.payment.domain.event.PaymentEvent;
import org.lowell.apps.payment.domain.event.PaymentEventProducer;
import org.lowell.apps.payment.domain.model.EventStatus;
import org.lowell.apps.payment.domain.model.EventType;
import org.lowell.apps.payment.domain.model.PaymentOutBox;
import org.lowell.apps.payment.domain.repository.PaymentOutBoxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentOutBoxService {
    private final ObjectMapper objectMapper;
    private final PaymentOutBoxRepository paymentOutBoxRepository;
    private final PaymentEventProducer paymentEventProducer;

    @Transactional
    public void save(PaymentEvent.CompletedPayment event) {
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("eventId", event.getEventId());
        payload.put("token", event.getToken());

        PaymentOutBox eventOutBox = PaymentOutBox.create(event.getEventId(), EventType.PAYMENT_COMPLETED, payload);
        paymentOutBoxRepository.save(eventOutBox);
    }

    public void publishEvent(PaymentEvent.CompletedPayment event) {
        paymentEventProducer.sendMessage(event);
    }

    @Transactional
    public void successPaymentOutBox(PaymentOutBoxQuery.Get query) {
        PaymentOutBox paymentOutBox = paymentOutBoxRepository.getPaymentOutBox(query);
        paymentOutBox.success();
    }

    public List<PaymentOutBox> getRepublishPaymentOutBoxList() {
        PaymentOutBoxQuery.GetList query = new PaymentOutBoxQuery.GetList(EventStatus.INIT, LocalDateTime.now().minusMinutes(5));
        return paymentOutBoxRepository.getPaymentOutBoxList(query);
    }

    public void republishPaymentOutBox() {
        List<PaymentOutBox> items = getRepublishPaymentOutBoxList();
        log.info("### Republish PaymentOutBox Count[{}]", items.size());
        for (PaymentOutBox item : items) {
            publishEvent(PaymentEvent.CompletedPayment.of(item.getEventId(),
                                                          item.getPayload()
                                                              .get("token")
                                                              .toString()));
        }
    }

}

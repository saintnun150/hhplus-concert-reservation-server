package org.lowell.apps.payment.domain.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.apps.payment.domain.service.PaymentOutBoxService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final PaymentOutBoxService paymentOutBoxService;

    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void savePaymentEventOutBox(PaymentEvent.CompletedPayment event) {
        paymentOutBoxService.save(event);
    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void publishEvent(PaymentEvent.CompletedPayment event) {
        paymentOutBoxService.publishEvent(event);
    }
}

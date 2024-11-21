package org.lowell.apps.payment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOutBoxScheduler {
    private final PaymentFacade paymentFacade;

    @Scheduled(fixedRate = 1000 * 60)
    public void processRepublishPaymentOutBox() {
        try {
            log.info("## [Republish Scheduler] Republish payment outbox event");
            paymentFacade.republishEvent();
        } catch (Exception e) {
            log.error("## Error occurred while republish payment outbox event", e);
        }
    }
}

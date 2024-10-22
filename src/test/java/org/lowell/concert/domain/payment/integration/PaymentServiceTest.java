package org.lowell.concert.domain.payment.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.payment.dto.PaymentCommand;
import org.lowell.concert.domain.payment.exception.PaymentErrorCode;
import org.lowell.concert.domain.payment.model.PaymentStatus;
import org.lowell.concert.domain.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @DisplayName("없는 결제 번호 조회 시 예외가 발생한다.")
    @Test
    void shouldFailPaymentWhenReservationIdIsInvalid() {

        paymentService.createPayment(new PaymentCommand.Create(1L, 1000L, PaymentStatus.APPROVED));
        assertThatThrownBy(() -> paymentService.getPayment(2L))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(PaymentErrorCode.NOT_FOUND_PAYMENT);
                });
    }

}
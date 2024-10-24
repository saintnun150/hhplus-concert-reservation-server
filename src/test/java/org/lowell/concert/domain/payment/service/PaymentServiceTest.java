package org.lowell.concert.domain.payment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.payment.dto.PaymentCommand;
import org.lowell.concert.domain.payment.exception.PaymentErrorCode;
import org.lowell.concert.domain.payment.exception.PaymentException;
import org.lowell.concert.domain.payment.model.PaymentStatus;
import org.lowell.concert.domain.payment.repository.PaymentRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @DisplayName("예약 ID가 잘못되었을 때 결제가 실패하며 예외가 발생한다.")
    @Test
    void shouldFailPaymentWhenReservationIdIsInvalid() {
        Long reservationId = null;
        PaymentCommand.Create command = new PaymentCommand.Create(reservationId, 1000L, PaymentStatus.APPROVED);
        assertThatThrownBy(() -> paymentService.createPayment(command))
                .isInstanceOfSatisfying(PaymentException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(PaymentErrorCode.INVALID_RESERVATION_ID);
                });
    }

}
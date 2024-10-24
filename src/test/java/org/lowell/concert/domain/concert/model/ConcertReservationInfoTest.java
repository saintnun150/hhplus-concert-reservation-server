package org.lowell.concert.domain.concert.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.concert.exception.ConcertReservationErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertReservationException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConcertReservationInfoTest {

    @DisplayName("결제 상태로 전환하려고 할 때 결제 가능한 예약상태가 아니라면) 예외를 반환한다.")
    @Test
    void throwsException_WhenStatusIsNotReserved() {
        ConcertReservationInfo info = ConcertReservationInfo.builder()
                                                            .reservationId(1L)
                                                            .seatId(1L)
                                                            .userId(1L)
                                                            .status(ReservationStatus.CONFIRMED)
                                                            .reservedAt(LocalDateTime.now())
                                                            .build();

        assertThatThrownBy(info::checkPayableReservationStatus)
                .isInstanceOfSatisfying(ConcertReservationException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertReservationErrorCode.STATE_COMPLETE);
                });
    }
}


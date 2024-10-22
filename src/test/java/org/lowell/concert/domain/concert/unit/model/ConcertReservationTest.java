package org.lowell.concert.domain.concert.unit.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.concert.model.ConcertReservation;
import org.lowell.concert.domain.concert.model.ReservationStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConcertReservationTest {

    @DisplayName("결제를 생성한다.")
    @Test
    void createPayableReservation() {
        ConcertReservation info = ConcertReservation.builder()
                                                    .reservationId(1L)
                                                    .seatId(1L)
                                                    .userId(1L)
                                                    .status(ReservationStatus.PENDING)
                                                    .createdAt(LocalDateTime.now())
                                                    .reservedAt(LocalDateTime.now())
                                                    .build();

        assertThat(info.getStatus()).isEqualTo(ReservationStatus.PENDING);
    }

    @DisplayName("결제 완료 상태로 변경한다.")
    @Test
    void confirmReservation() {
        ConcertReservation reservation = ConcertReservation.builder()
                                                           .reservationId(1L)
                                                           .seatId(1L)
                                                           .userId(1L)
                                                           .status(ReservationStatus.PENDING)
                                                           .reservedAt(LocalDateTime.now())
                                                           .build();
        reservation.isReservableStatus();
        reservation.completeReservation(LocalDateTime.now());
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.COMPLETED);
    }

    @DisplayName("예약 완료 처리하려고 할 때 해당 좌석의 예약 만료시간이 지나 예약을 못하게 될 경우 예약을 만료처리한다.")
    @Test
    void throwsException_WhenExpired() {
        ConcertReservation reservation = ConcertReservation.builder()
                                                           .reservationId(1L)
                                                           .seatId(1L)
                                                           .userId(1L)
                                                           .status(ReservationStatus.PENDING)
                                                           .reservedAt(LocalDateTime.now().minusMinutes(10))
                                                           .build();
        reservation.expireReservation(LocalDateTime.now());
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.EXPIRED);
    }
}


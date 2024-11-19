package org.lowell.apps.domain.concert.unit.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.concert.domain.exception.ConcertSeatError;
import org.lowell.apps.concert.domain.model.ConcertSeat;
import org.lowell.apps.concert.domain.model.SeatStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConcertSeatTest {

    @DisplayName("최종 예약된 좌석은 예외를 반환한다.")
    @Test
    void throwException_when_search_concert_seats_reserved_complete() {
        long concertDateId = 1L;
        int seatNo = 1;
        LocalDateTime now = LocalDateTime.now();
        ConcertSeat concertSeat = ConcertSeat.builder()
                                      .seatId(1L)
                                      .concertScheduleId(concertDateId)
                                      .seatNo(seatNo)
                                      .status(SeatStatus.OCCUPIED)
                                      .price(10000)
                                      .tempReservedAt(LocalDateTime.now().minusMinutes(3))
                                      .reservedAt(LocalDateTime.now())
                                      .build();

        assertThatThrownBy(() -> concertSeat.checkReservableSeat(now, 5))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertSeatError.RESERVED_COMPLETE);
                });
    }

    @DisplayName("임시 예약된 좌석인데 배정시간이 만료되면 예외를 반환한다.")
    @Test
    void throwException_when_concertSeatTempReserved_and_TimeExpired() {
        long concertDateId = 1L;
        int seatNo = 1;
        int tempReservedMinutes = 5;
        LocalDateTime now = LocalDateTime.now();
        ConcertSeat concertSeat = ConcertSeat.builder()
                                      .seatId(1L)
                                      .concertScheduleId(concertDateId)
                                      .seatNo(seatNo)
                                      .status(SeatStatus.OCCUPIED)
                                      .price(10000)
                                      .tempReservedAt(LocalDateTime.now().minusMinutes(6))
                                      .reservedAt(null)
                                      .build();

        assertThatThrownBy(() -> concertSeat.checkTemporaryReservedExpired(now, tempReservedMinutes))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertSeatError.RESERVED_EXPIRED);
                });
    }

    @DisplayName("좌석 임시 예약이 완료된다.")
    @Test
    void completeTemporaryReservation() {
        long concertDateId = 1L;
        int seatNo = 1;
        int tempReservedMinutes = 5;
        LocalDateTime now = LocalDateTime.now();
        ConcertSeat concertSeat = ConcertSeat.builder()
                                      .seatId(1L)
                                      .concertScheduleId(concertDateId)
                                      .seatNo(seatNo)
                                      .status(SeatStatus.EMPTY)
                                      .price(10000)
                                      .tempReservedAt(null)
                                      .reservedAt(null)
                                      .build();

        concertSeat.checkReservableSeat(now, tempReservedMinutes);
        concertSeat.reserveSeatTemporarily(now);

        assertThat(concertSeat.getTempReservedAt()).isNotNull();
        assertThat(concertSeat.getStatus()).isEqualTo(SeatStatus.OCCUPIED);

    }

    @DisplayName("좌석 예약이 완료된다.")
    @Test
    void completeReservation() {
        long concertDateId = 1L;
        int seatNo = 1;
        ConcertSeat concertSeat = ConcertSeat.builder()
                                      .seatId(1L)
                                      .concertScheduleId(concertDateId)
                                      .seatNo(seatNo)
                                      .status(SeatStatus.OCCUPIED)
                                      .price(10000)
                                      .tempReservedAt(null)
                                      .reservedAt(null)
                                      .build();
        concertSeat.reserveSeat(LocalDateTime.now());
        assertThat(concertSeat.getReservedAt()).isNotNull();
    }
  
}
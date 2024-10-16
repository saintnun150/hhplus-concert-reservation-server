package org.lowell.concert.domain.concert.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.concert.exception.ConcertSeatErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertSeatException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ConcertSeatInfoTest {

    @DisplayName("최종 예약된 좌석은 예외를 반환한다.")
    @Test
    void throwException_when_search_concert_seats_reserved_complete() {
        long concertDateId = 1L;
        int seatNo = 1;
        LocalDateTime now = LocalDateTime.now();
        ConcertSeatInfo info = ConcertSeatInfo.builder()
                                              .seatId(1L)
                                              .concertDateId(concertDateId)
                                              .seatNo(seatNo)
                                              .status(SeatStatus.OCCUPIED)
                                              .price(10000)
                                              .tempReservedAt(LocalDateTime.now().minusMinutes(3))
                                              .reservedAt(LocalDateTime.now())
                                              .build();

        assertThatThrownBy(() -> info.checkReserveStatus(now, 5))
                .isInstanceOfSatisfying(ConcertSeatException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertSeatErrorCode.RESERVED_COMPLETE);
                });
    }

    @DisplayName("임시 예약된 좌석인데 배정시간이 만료되면 예외를 반환한다.")
    @Test
    void throwException_when_concertSeatTempReserved_and_TimeExpired() {
        long concertDateId = 1L;
        int seatNo = 1;
        int tempReservedMinutes = 5;
        LocalDateTime now = LocalDateTime.now();
        ConcertSeatInfo info = ConcertSeatInfo.builder()
                                              .seatId(1L)
                                              .concertDateId(concertDateId)
                                              .seatNo(seatNo)
                                              .status(SeatStatus.OCCUPIED)
                                              .price(10000)
                                              .tempReservedAt(LocalDateTime.now().minusMinutes(6))
                                              .reservedAt(null)
                                              .build();

        assertThatThrownBy(() -> info.checkReserveStatus(now, tempReservedMinutes))
                .isInstanceOfSatisfying(ConcertSeatException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertSeatErrorCode.RESERVED_TEMPORARY);
                });
    }

    @DisplayName("임시 예약된 좌석인데 배정시간이 만료되지 않으면 예외를 반환하지 않는다.")
    @Test
    void throwException_when_concertSeatTempReserved_and_TimeNotExpired() {
        long concertDateId = 1L;
        int seatNo = 1;
        int tempReservedMinutes = 5;
        LocalDateTime now = LocalDateTime.now();
        ConcertSeatInfo info = ConcertSeatInfo.builder()
                                              .seatId(1L)
                                              .concertDateId(concertDateId)
                                              .seatNo(seatNo)
                                              .status(SeatStatus.OCCUPIED)
                                              .price(10000)
                                              .tempReservedAt(LocalDateTime.now().minusMinutes(2))
                                              .reservedAt(null)
                                              .build();

        assertDoesNotThrow(() -> info.checkReserveStatus(now, tempReservedMinutes));
    }
  
}
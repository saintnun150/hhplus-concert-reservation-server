package org.lowell.concert.application.concert;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.model.ReservationStatus;
import org.lowell.concert.domain.concert.model.SeatStatus;

import java.time.LocalDateTime;

public class ConcertInfo {

    @Getter
    @RequiredArgsConstructor
    public static class ScheduleInfo {
        private final Long scheduleId;
        private final Long concertId;
        private final LocalDateTime scheduleDate;
        private final LocalDateTime beginTime;
        private final LocalDateTime endTime;
        private final LocalDateTime createdAt;
    }

    @Getter
    @RequiredArgsConstructor
    public static class SeatInfo {
        private final Long seatId;
        private final Long concertScheduleId;
        private final int seatNo;
        private final SeatStatus status;
        private final int price;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ReservationInfo {
        private final Long reservationId;
        private final int seatNo;
        private final Long userId;
        private final ReservationStatus status;
        private final LocalDateTime createdAt;
    }
}

package org.lowell.concert.application.concert;

import org.lowell.concert.domain.concert.model.ReservationStatus;
import org.lowell.concert.domain.concert.model.SeatStatus;

import java.time.LocalDateTime;

public class ConcertInfo {
    public record Info(Long concertId, String name, LocalDateTime openedAt) {
        public static Info of(Long concertId, String name, LocalDateTime openedAt) {
            return new Info(concertId, name, openedAt);
        }
    }

    public record ScheduleInfo(Long scheduleId, Long concertId, LocalDateTime scheduleDate, LocalDateTime beginTime, LocalDateTime endTime, LocalDateTime createdAt) {
        public static ScheduleInfo of(Long scheduleId, Long concertId, LocalDateTime scheduleDate, LocalDateTime beginTime, LocalDateTime endTime, LocalDateTime createdAt) {
            return new ScheduleInfo(scheduleId, concertId, scheduleDate, beginTime, endTime, createdAt);
        }
    }

    public record SeatInfo(Long seatId, Long concertScheduleId, int seatNo, SeatStatus status, long price) {
        public static SeatInfo of(Long seatId, Long concertScheduleId, int seatNo, SeatStatus status, long price) {
            return new SeatInfo(seatId, concertScheduleId, seatNo, status, price);
        }
    }

    public record ReservationInfo(Long reservationId, int seatNo, Long userId, ReservationStatus status, LocalDateTime createdAt) {
        public static ReservationInfo of(Long reservationId, int seatNo, Long userId, ReservationStatus status, LocalDateTime createdAt) {
            return new ReservationInfo(reservationId, seatNo, userId, status, createdAt);
        }
    }
}

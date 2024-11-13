package org.lowell.concert.interfaces.api.concert;

import io.swagger.v3.oas.annotations.media.Schema;
import org.lowell.concert.application.concert.ConcertInfo;
import org.lowell.concert.domain.concert.model.ReservationStatus;

import java.time.LocalDateTime;

public class ConcertResponse {
    public record Info(@Schema(description = "콘서트 ID") Long concertId,
                       @Schema(description = "콘서트 이름") String concertName,
                       @Schema(description = "콘서트 오픈일") LocalDateTime openedAt) {
        public static Info of(ConcertInfo.Info info) {
            return new Info(info.concertId(), info.name(), info.openedAt());
        }
    }

    public record ScheduleInfo(@Schema(description = "콘서트 날짜 ID") Long scheduleId,
                               @Schema(description = "콘서트 일자") LocalDateTime scheduleDate,
                               @Schema(description = "콘서트 시작시간") LocalDateTime beginTime,
                               @Schema(description = "콘서트 종료시간") LocalDateTime endTime) {
        public static ScheduleInfo of(ConcertInfo.ScheduleInfo scheduleInfo) {
            return new ScheduleInfo(scheduleInfo.scheduleId(),
                                    scheduleInfo.scheduleDate(),
                                    scheduleInfo.beginTime(),
                                    scheduleInfo.endTime());
        }

    }

    public record SeatInfo(@Schema(description = "콘서트 좌석 ID") Long seatId,
                           @Schema(description = "좌석 가격") int seatNo,
                           @Schema(description = "좌석 가격") long price) {
        public static SeatInfo of(ConcertInfo.SeatInfo seatInfo) {
            return new SeatInfo(seatInfo.seatId(),
                                seatInfo.seatNo(),
                                seatInfo.price());
        }
    }

    public record ReservationInfo(@Schema(description = "콘서트 예약 ID") Long reservationId,
                                  @Schema(description = "콘서트 좌석 번호") int seatNo,
                                  @Schema(description = "예약자 ID") Long userId,
                                  @Schema(description = "예약 상태") ReservationStatus status,
                                  @Schema(description = "예약 생성일") LocalDateTime createdAt) {

        public static ReservationInfo of(ConcertInfo.ReservationInfo reservationInfo) {
            return new ReservationInfo(reservationInfo.reservationId(),
                                       reservationInfo.seatNo(),
                                       reservationInfo.userId(),
                                       reservationInfo.status(),
                                       reservationInfo.createdAt());
        }
    }
}

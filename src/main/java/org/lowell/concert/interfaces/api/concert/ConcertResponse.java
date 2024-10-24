package org.lowell.concert.interfaces.api.concert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.lowell.concert.domain.concert.model.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConcertResponse {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Info {
        @Schema(description = "콘서트 ID")
        private final Long concertId;
        @Schema(description = "콘서트 이름")
        private final String concertName;
        @Schema(description = "콘서트 생성일")
        private final LocalDate createdAt;
        @Schema(description = "콘서트 종료일")
        private final LocalDate deletedAt;
    }


    @Getter
    @AllArgsConstructor
    @Builder
    public static class ScheduleInfo {
        @Schema(description = "콘서트 날짜 ID")
        private final Long scheduleId;
        @Schema(description = "콘서트 일자")
        private final LocalDateTime scheduleDate;
        @Schema(description = "콘서트 시작시간")
        private final LocalDateTime startTime;
        @Schema(description = "콘서트 종료시간")
        private final LocalDateTime endTime;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SeatInfo {
        @Schema(description = "콘서트 좌석 ID")
        private final Long seatId;
        @Schema(description = "좌석 가격")
        private final int seatNo;
        @Schema(description = "좌석 가격")
        private final long price;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReservationInfo {
        @Schema(description = "콘서트 예약 ID")
        private final Long reservationId;
        @Schema(description = "콘서트 좌석 번호")
        private final int seatNo;
        @Schema(description = "예약자 ID")
        private final Long userId;
        @Schema(description = "예약 상태")
        private final ReservationStatus status;
        @Schema(description = "예약 생성일")
        private final LocalDateTime createdAt;
    }
}

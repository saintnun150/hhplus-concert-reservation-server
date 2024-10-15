package org.lowell.concert.interfaces.api.concert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    public static class DateInfo {
        @Schema(description = "콘서트 날짜 ID")
        private final Long concertDateId;
        @Schema(description = "콘서트 일자")
        private final LocalDateTime concertDate;
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
        private final Long seatNo;
        @Schema(description = "좌석 가격")
        private final Long price;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReservationInfo {
        @Schema(description = "예약 ID")
        private final Long concertReservationId;
    }
}

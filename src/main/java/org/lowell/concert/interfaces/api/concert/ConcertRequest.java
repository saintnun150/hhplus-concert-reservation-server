package org.lowell.concert.interfaces.api.concert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ConcertRequest {

    @Getter
    @AllArgsConstructor
    public static class GetConcert {
        @Schema(description = "콘서트명")
        private String concertName;
        @Schema(description = "콘서트 시작일자 조회 시작일")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime from;
        @Schema(description = "콘서트 시작일자 조회 종료일")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime to;
    }

    @Getter
    @AllArgsConstructor
    public static class GetSchedules {
        @Schema(description = "콘서트 id")
        private String concertId;
        @Schema(description = "콘서트 스케쥴 검색 시작일")
        private LocalDateTime from;
        @Schema(description = "콘서트 스케쥴 검색 종료일")
        private LocalDateTime to;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchSeat {
        @Schema(description = "콘서트 날짜 ID")
        private Long concertScheduleId;
    }

    @Getter
    @AllArgsConstructor
    public static class Reservation {
        @Schema(description = "좌석 ID")
        private Long seatId;
        @Schema(description = "예약자 id")
        private Long userId;
    }
}

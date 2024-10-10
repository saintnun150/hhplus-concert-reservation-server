package org.lowell.concert.interfaces.api.concert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ConcertRequest {

    @Getter
    @AllArgsConstructor
    public static class SearchDate {
        @Schema(description = "콘서트 시작일자 조회 시작일")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime from;
        @Schema(description = "콘서트 시작일자 조회 종료일")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime to;
    }

    @Getter
    @AllArgsConstructor
    public static class SearchSeat {
        @Schema(description = "콘서트 날짜 ID")
        private Long concertDateId;
    }

    @Getter
    @AllArgsConstructor
    public static class Reservation {
        @Schema(description = "좌석 ID")
        private Long seatId;
    }
}

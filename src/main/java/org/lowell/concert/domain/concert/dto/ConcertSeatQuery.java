package org.lowell.concert.domain.concert.dto;

import java.time.LocalDateTime;

public class ConcertSeatQuery {
    public record Search(Long seatId) {
    }

    public record SearchList(Long concertScheduleId, LocalDateTime now) {
    }
}
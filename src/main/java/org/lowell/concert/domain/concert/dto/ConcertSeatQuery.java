package org.lowell.concert.domain.concert.dto;

public class ConcertSeatQuery {
    public record Search(Long seatId) {
    }

    public record SearchList(Long concertScheduleId) {
    }

    public record SearchAvailableList(Long concertScheduleId) {
    }
}
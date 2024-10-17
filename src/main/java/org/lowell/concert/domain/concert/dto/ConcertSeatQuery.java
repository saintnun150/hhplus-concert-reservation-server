package org.lowell.concert.domain.concert.dto;

public class ConcertSeatQuery {
    public record Search(Long concertDateId, int seatNo) {
    }

    public record SearchList(Long concertDateId) {
    }
}
package org.lowell.concert.domain.concert.dto;

public class ConcertReservationQuery {
    public record Search(Long concertReservationId) {
    }
    public record SearchList(Long userId) {
    }
}
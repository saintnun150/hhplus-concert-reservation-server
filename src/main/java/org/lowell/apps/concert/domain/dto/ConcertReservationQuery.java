package org.lowell.apps.concert.domain.dto;

public class ConcertReservationQuery {
    public record Search(Long concertReservationId) {
    }
    public record SearchList(Long userId) {
    }
}
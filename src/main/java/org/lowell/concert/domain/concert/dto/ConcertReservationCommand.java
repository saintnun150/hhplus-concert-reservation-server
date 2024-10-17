package org.lowell.concert.domain.concert.dto;

import org.lowell.concert.domain.concert.model.ReservationStatus;

public class ConcertReservationCommand {
    public record Create(Long seatId, Long userId) {
    }

    public record Update(Long concertReservationId, ReservationStatus status) {

    }
}
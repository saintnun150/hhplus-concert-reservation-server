package org.lowell.apps.concert.domain.dto;

import org.lowell.apps.concert.domain.model.ReservationStatus;

public class ConcertReservationCommand {
    public record Create(Long seatId, Long userId) {
    }

    public record Update(Long concertReservationId, ReservationStatus status) {

    }
}
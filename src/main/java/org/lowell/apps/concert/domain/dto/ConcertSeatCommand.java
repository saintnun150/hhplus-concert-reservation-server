package org.lowell.apps.concert.domain.dto;

import org.lowell.apps.concert.domain.model.SeatStatus;

public class ConcertSeatCommand {
    public record Create(Long scheduleId, int seatNo, SeatStatus status, int price) {
    }
}
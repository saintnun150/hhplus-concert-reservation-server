package org.lowell.concert.domain.concert.dto;

import org.lowell.concert.domain.concert.model.SeatStatus;

public class ConcertSeatCommand {
    public record Create(Long scheduleId, int seatNo, SeatStatus status, int price) {
    }
}
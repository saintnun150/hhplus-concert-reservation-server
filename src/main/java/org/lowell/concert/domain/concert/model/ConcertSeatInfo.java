package org.lowell.concert.domain.concert.model;

import lombok.Builder;
import lombok.Getter;
import org.lowell.concert.domain.concert.exception.ConcertSeatErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertSeatException;

import java.time.LocalDateTime;

@Getter
@Builder
public class ConcertSeatInfo {
    private Long seatId;
    private Long concertDateId;
    private int seatNo;
    private SeatStatus status;
    private int price;
    private LocalDateTime tempReservedAt;
    private LocalDateTime reservedAt;

    public void checkReserveStatus(LocalDateTime now, int tempReservedMinutes) {
        if (reservedAt != null) {
            throw new ConcertSeatException(ConcertSeatErrorCode.RESERVED_COMPLETE);
        }
        if (tempReservedAt != null && tempReservedAt.plusMinutes(tempReservedMinutes).isBefore(now)) {
            throw new ConcertSeatException(ConcertSeatErrorCode.RESERVED_TEMPORARY);
        }
    }
}

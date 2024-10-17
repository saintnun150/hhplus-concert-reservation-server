package org.lowell.concert.domain.concert.model;

import lombok.Builder;
import lombok.Getter;
import org.lowell.concert.domain.concert.exception.ConcertReservationErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertReservationException;

import java.time.LocalDateTime;

@Getter
@Builder
public class ConcertReservationInfo {
    private Long reservationId;
    private Long seatId;
    private Long userId;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime reservedAt;

    public void checkPayableReservationStatus() {
        if (!ReservationStatus.RESERVED.equals(status) || reservedAt != null) {
            throw new ConcertReservationException(ConcertReservationErrorCode.STATE_COMPLETE);
        }

    }
}

package org.lowell.concert.domain.concert.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.exception.ConcertSeatErrorCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_concert_seat")
@Getter
@NoArgsConstructor
public class ConcertSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "concert_schedule_id")
    private Long concertScheduleId;

    @Column(name = "seat_no")
    private int seatNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SeatStatus status;

    @Column(name = "price")
    private int price;

    @Column(name = "temp_reserved_at")
    private LocalDateTime tempReservedAt;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Builder
    public ConcertSeat(Long seatId, Long concertScheduleId, int seatNo, SeatStatus status, int price, LocalDateTime tempReservedAt, LocalDateTime reservedAt) {
        this.seatId = seatId;
        this.concertScheduleId = concertScheduleId;
        this.seatNo = seatNo;
        this.status = status;
        this.price = price;
        this.tempReservedAt = tempReservedAt;
        this.reservedAt = reservedAt;
    }

    public boolean isEmpty() {
        return status == SeatStatus.EMPTY;
    }

    public void isCompletedReserved() {
        if (reservedAt != null) {
            throw new DomainException(ConcertSeatErrorCode.RESERVED_COMPLETE);
        }
    }

    public void isTemporaryReserved(LocalDateTime now, int tempReservedMinutes) {
        if (tempReservedAt != null && tempReservedAt.plusMinutes(tempReservedMinutes).isBefore(now)) {
            throw new DomainException(ConcertSeatErrorCode.RESERVED_TEMPORARY);
        }
    }

    public void checkAvailableSeat(LocalDateTime now, int tempReservedMinutes) {
        isCompletedReserved();
        isTemporaryReserved(now, tempReservedMinutes);
    }

    public void reserveSeatTemporarily(LocalDateTime time) {
        this.status = SeatStatus.OCCUPIED;
        this.tempReservedAt = time;
    }

    public void reserveSeat(LocalDateTime time) {
        this.reservedAt = time;
    }
}

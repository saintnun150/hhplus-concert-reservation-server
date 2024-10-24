package org.lowell.concert.domain.concert.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.exception.ConcertSeatError;

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
    private long price;

    @Column(name = "temp_reserved_at")
    private LocalDateTime tempReservedAt;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Builder
    public ConcertSeat(Long seatId, Long concertScheduleId, int seatNo, SeatStatus status, long price, LocalDateTime tempReservedAt, LocalDateTime reservedAt) {
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

    public boolean isTemporaryReserved(LocalDateTime now, int tempReservedMinutes) {
        if (tempReservedAt == null) {
            return false;
        }
        if (tempReservedAt.plusMinutes(tempReservedMinutes).isAfter(now)) {
            return false;
        }
        return true;
    }

    public void isCompletedReserved() {
        if (reservedAt != null) {
            throw DomainException.create(ConcertSeatError.RESERVED_COMPLETE);
        }
    }

    public void checkTemporaryReservedExpired(LocalDateTime now, int tempReservedMinutes) {
        if (tempReservedAt != null && tempReservedAt.plusMinutes(tempReservedMinutes).isBefore(now)) {
            throw DomainException.create(ConcertSeatError.RESERVED_EXPIRED,
                                         DomainException.createPayload(now, tempReservedMinutes));
        }
    }

//    public void isTemporaryReserved(LocalDateTime now, int tempReservedMinutes) {
//        if (tempReservedAt != null && tempReservedAt.plusMinutes(tempReservedMinutes).isAfter(now)) {
//            throw DomainException(ConcertSeatError.RESERVED_TEMPORARY, DomainException.createPayload(now, tempReservedMinutes));
//        }
//    }

    public void checkAvailableSeat(LocalDateTime now, int tempReservedMinutes) {
        isCompletedReserved();
        checkTemporaryReservedExpired(now, tempReservedMinutes);
    }

    public void reserveSeatTemporarily(LocalDateTime time) {
        this.status = SeatStatus.OCCUPIED;
        this.tempReservedAt = time;
    }

    public void reserveSeat(LocalDateTime time) {
        this.reservedAt = time;
    }
}

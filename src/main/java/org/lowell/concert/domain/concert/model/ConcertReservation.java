package org.lowell.concert.domain.concert.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.exception.ConcertReservationError;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_concert_reservation")
@Getter
@NoArgsConstructor
public class ConcertReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Builder
    public ConcertReservation(Long reservationId, Long seatId, Long userId, ReservationStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime reservedAt) {
        this.reservationId = reservationId;
        this.seatId = seatId;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.reservedAt = reservedAt;
    }

    public void completeReservation(LocalDateTime time) {
        this.status = ReservationStatus.COMPLETED;
        this.updatedAt = time;
        this.reservedAt = time;
    }

    public void expireReservation(LocalDateTime time) {
        if (status != ReservationStatus.EXPIRED) {
            this.status = ReservationStatus.EXPIRED;
            this.updatedAt = time;
        }
    }

    public void isReservableStatus() {
        if (status == ReservationStatus.EXPIRED) {
            throw DomainException.create(ConcertReservationError.STATE_EXPIRED);
        }
        if (status == ReservationStatus.COMPLETED) {
            throw DomainException.create(ConcertReservationError.STATE_COMPLETE);
        }
    }
}

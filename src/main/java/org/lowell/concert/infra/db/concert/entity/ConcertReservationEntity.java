package org.lowell.concert.infra.db.concert.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lowell.concert.domain.concert.model.ReservationStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_concert_reservation")
@Getter
@NoArgsConstructor
public class ConcertReservationEntity {

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
    public ConcertReservationEntity(Long reservationId, Long seatId, Long userId, ReservationStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime reservedAt) {
        this.reservationId = reservationId;
        this.seatId = seatId;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.reservedAt = reservedAt;
    }

    public void updateConcertReservationState(ReservationStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
        if (ReservationStatus.CONFIRMED.equals(status)) {
            this.reservedAt = LocalDateTime.now();
        }
    }
}

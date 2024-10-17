package org.lowell.concert.infra.db.concert.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lowell.concert.domain.concert.model.SeatStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_concert_seat")
@Getter
@NoArgsConstructor
public class ConcertSeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "concert_date_id")
    private Long concertDateId;

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
    public ConcertSeatEntity(Long seatId, Long concertDateId, int seatNo, SeatStatus status, int price, LocalDateTime tempReservedAt, LocalDateTime reservedAt) {
        this.seatId = seatId;
        this.concertDateId = concertDateId;
        this.seatNo = seatNo;
        this.status = status;
        this.price = price;
        this.tempReservedAt = tempReservedAt;
        this.reservedAt = reservedAt;
    }
}

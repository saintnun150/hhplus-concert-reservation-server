package org.lowell.concert.infra.db.concert.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_concert_date")
@Getter
@NoArgsConstructor
public class ConcertDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_date_id")
    private Long concertDateId;

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "concert_date")
    private LocalDateTime concertDate;

    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public ConcertDateEntity(Long concertDateId, Long concertId, LocalDateTime concertDate, LocalDateTime beginTime, LocalDateTime endTime, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.concertDateId = concertDateId;
        this.concertId = concertId;
        this.concertDate = concertDate;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
}

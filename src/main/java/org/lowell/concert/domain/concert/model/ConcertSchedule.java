package org.lowell.concert.domain.concert.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_concert_date")
@Getter
@NoArgsConstructor
public class ConcertSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_schedule_id")
    private Long scheduleId;

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "schedule_date")
    private LocalDateTime scheduleDate;

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
    public ConcertSchedule(Long scheduleId, Long concertId, LocalDateTime scheduleDate, LocalDateTime beginTime, LocalDateTime endTime, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.scheduleId = scheduleId;
        this.concertId = concertId;
        this.scheduleDate = scheduleDate;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
}

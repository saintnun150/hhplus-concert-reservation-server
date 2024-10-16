package org.lowell.concert.domain.concert.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ConcertDateInfo {
    private Long concertDateId;
    private Long concertId;
    private LocalDateTime concertDate;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}

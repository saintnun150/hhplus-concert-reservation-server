package org.lowell.concert.domain.concert.dto;

import java.time.LocalDateTime;

public class ConcertScheduleCommand {
    public record Create(Long concertId, LocalDateTime scheduleDate, LocalDateTime beginTime, LocalDateTime endTime) {
    }
}

package org.lowell.apps.concert.domain.dto;

import java.time.LocalDateTime;

public class ConcertScheduleQuery {
    public record Search(Long scheduleId){ }
    public record SearchList(Long concertId, LocalDateTime from, LocalDateTime to) { }
}

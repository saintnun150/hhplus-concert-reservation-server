package org.lowell.concert.domain.concert.dto;

import java.time.LocalDateTime;

public class ConcertScheduleQuery {
    public record Search(Long scheduleId){

    }
    public record SearchList(Long concertId, LocalDateTime scheduleDate) {
    }
}

package org.lowell.concert.domain.concert.dto;

import java.time.LocalDateTime;

public class ConcertDateCommand {
    public record Create(Long concertId, LocalDateTime concertDate, LocalDateTime beginTime, LocalDateTime endTime) {
    }
}

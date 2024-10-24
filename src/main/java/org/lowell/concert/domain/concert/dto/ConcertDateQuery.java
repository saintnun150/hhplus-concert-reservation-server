package org.lowell.concert.domain.concert.dto;

import java.time.LocalDateTime;

public class ConcertDateQuery {
    public record SearchList(Long concertId, LocalDateTime concertDate) {
    }
}

package org.lowell.concert.domain.concert.dto;

import java.time.LocalDateTime;

public class ConcertQuery {
    public record SearchList(String name, LocalDateTime from, LocalDateTime to) { }
}

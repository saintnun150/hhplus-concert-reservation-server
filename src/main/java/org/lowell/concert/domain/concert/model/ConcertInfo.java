package org.lowell.concert.domain.concert.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class ConcertInfo {
    private final Long concertId;
    private final String name;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;
}

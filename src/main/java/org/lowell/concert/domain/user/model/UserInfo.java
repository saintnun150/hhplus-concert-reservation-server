package org.lowell.concert.domain.user.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserInfo {
    private final Long userId;
    private final String username;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}

package org.lowell.concert.domain.waitingqueue.dto;

import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;

import java.time.LocalDateTime;
import java.util.List;

public class WaitingQueueTokenCommand {
    public record Create(String token, TokenStatus status) {}

    public record Update(List<Long> tokenIds, TokenStatus status, LocalDateTime updatedAt, LocalDateTime expiresAt) { }
}

package org.lowell.concert.domain.waitingqueue.dto;

import org.lowell.concert.domain.waitingqueue.model.TokenStatus;

import java.time.LocalDateTime;
import java.util.List;

public class WaitingQueueCommand {
    public record CreateToken(String token, TokenStatus status, LocalDateTime expiresAt) {
    }
    public record UpdateBatch(List<Long> tokenIds, TokenStatus status, LocalDateTime updatedAt, LocalDateTime expiresAt) { }
    public record ExpireToken(String token, LocalDateTime now) { }
}

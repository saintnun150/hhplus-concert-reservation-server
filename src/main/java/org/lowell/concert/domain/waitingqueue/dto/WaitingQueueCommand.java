package org.lowell.concert.domain.waitingqueue.dto;

import org.lowell.concert.domain.waitingqueue.model.TokenStatus;

import java.time.LocalDateTime;
import java.util.List;

public class WaitingQueueCommand {
    public record Create(String token, TokenStatus status, LocalDateTime expiresAt) {
    }
    public record Update(Long tokenId, TokenStatus status, LocalDateTime updatedAt) {
    }
    public record UpdateBatch(List<Long> tokenIds, TokenStatus status, LocalDateTime updatedAt, LocalDateTime expiresAt) { }
}

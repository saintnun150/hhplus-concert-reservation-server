package org.lowell.concert.domain.waitingqueue.dto;

import org.lowell.concert.domain.waitingqueue.model.TokenStatus;

import java.time.LocalDateTime;
import java.util.List;

public class WaitingQueueCommand {
    public record CreateToken(String token, TokenStatus status, LocalDateTime expiresAt) {
    }
    public record UpdateBatch(Long count) { }
    public record ExpireToken(String token, LocalDateTime now) { }
}

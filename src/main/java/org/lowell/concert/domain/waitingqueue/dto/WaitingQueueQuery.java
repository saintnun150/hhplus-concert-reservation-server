package org.lowell.concert.domain.waitingqueue.dto;

import org.lowell.concert.domain.waitingqueue.model.TokenStatus;

import java.time.LocalDateTime;

public class WaitingQueueQuery {
    public record GetToken(String token) { }
    public record GetQueues(TokenStatus tokenStatus, LocalDateTime expiresAt, long size) { }
}

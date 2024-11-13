package org.lowell.concert.domain.waitingqueue.dto;

import java.time.LocalDateTime;

public class WaitingQueueQuery {
    public record GetToken(String token) { }
    public record GetQueues(long size, LocalDateTime expiresAt) { }
}

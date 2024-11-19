package org.lowell.apps.waitingqueue.domain.dto;

import java.time.LocalDateTime;

public class WaitingQueueQuery {
    public record GetToken(String token) { }
    public record GetQueues(long size, LocalDateTime expiresAt) { }
}

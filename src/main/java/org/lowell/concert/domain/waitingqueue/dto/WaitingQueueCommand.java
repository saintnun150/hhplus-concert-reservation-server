package org.lowell.concert.domain.waitingqueue.dto;

import java.time.LocalDateTime;

public class WaitingQueueCommand {
    public record Create(String token) { }
    public record ExpireToken(String token, LocalDateTime now) { }
}

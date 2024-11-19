package org.lowell.apps.waitingqueue.domain.dto;

import java.time.LocalDateTime;

public class WaitingQueueCommand {
    public record Create(String token) { }
    public record ExpireToken(String token, LocalDateTime now) { }
}

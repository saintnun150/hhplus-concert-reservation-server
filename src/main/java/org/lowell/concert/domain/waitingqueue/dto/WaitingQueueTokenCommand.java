package org.lowell.concert.domain.waitingqueue.dto;

import org.lowell.concert.domain.waitingqueue.model.TokenStatus;

public class WaitingQueueTokenCommand {
    public record Create(String token, TokenStatus status) {
    }
}

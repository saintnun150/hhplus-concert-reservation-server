package org.lowell.concert.domain.waitingqueue.dto;

import org.lowell.concert.domain.waitingqueue.model.TokenStatus;

public class WaitingQueueTokenQuery {
    public record Order(Long tokenId, TokenStatus tokenStatus) { }

    public record Update(TokenStatus tokenStatus, long size) { }
}

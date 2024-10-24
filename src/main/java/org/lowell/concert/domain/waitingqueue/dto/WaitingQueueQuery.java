package org.lowell.concert.domain.waitingqueue.dto;

import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueError;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.springframework.util.StringUtils;

public class WaitingQueueQuery {
    public record GetQueue(String token) {
        public GetQueue {
            if (!StringUtils.hasText(token)) {
                throw DomainException.create(WaitingQueueError.INVALID_TOKEN_INPUT);
            }
        }
    }

    public record Order(long tokenId, TokenStatus tokenStatus) {
    }

    public record GetQueues(TokenStatus tokenStatus, long size) { }
}

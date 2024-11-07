package org.lowell.concert.domain.waitingqueue.dto;

import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueError;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class WaitingQueueQuery {
    public record GetToken(String token) {
        public GetToken {
            if (!StringUtils.hasText(token)) {
                throw DomainException.create(WaitingQueueError.INVALID_TOKEN_INPUT);
            }
        }
    }
    public record GetOrder(String token, TokenStatus tokenStatus) { }
    public record CheckQueueActivation(String token, LocalDateTime now) {}
    public record GetQueues(TokenStatus tokenStatus, LocalDateTime expiresAt, long size) { }
}

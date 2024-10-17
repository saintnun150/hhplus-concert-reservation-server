package org.lowell.concert.domain.waitingqueue.model;

import lombok.Builder;
import lombok.Getter;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueTokenErrorCode;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueTokenException;

import java.time.LocalDateTime;

@Getter
@Builder
public class WaitingQueueTokenInfo {
    private final Long tokenId;
    private final String token;
    private final TokenStatus tokenStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime expiresAt;

    public void validateForWaiting() {
        if (tokenStatus != TokenStatus.WAITING) {
            throw new WaitingQueueTokenException(WaitingQueueTokenErrorCode.ILLEGAL_TOKEN_STATUS);
        }
    }
}

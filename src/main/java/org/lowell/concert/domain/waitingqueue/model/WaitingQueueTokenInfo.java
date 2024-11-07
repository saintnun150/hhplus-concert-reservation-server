package org.lowell.concert.domain.waitingqueue.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueError;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WaitingQueueTokenInfo {
    private final String token;
    private final TokenStatus tokenStatus;
    private final LocalDateTime expiresAt;

    public void validateWaitingStatus() {
        if (tokenStatus != TokenStatus.WAITING) {
            throw DomainException.create(WaitingQueueError.NOT_WAITING_STATUS);
        }
    }

    public boolean isActivateToken(LocalDateTime now, long timeToLive) {
        if (tokenStatus == TokenStatus.ACTIVATE) {
            ensureTokenIsExpired(now, timeToLive);
            return true;
        }
        return false;
    }

    public void ensureTokenStatusIsActive() {
        if (tokenStatus != TokenStatus.ACTIVATE) {
            throw DomainException.create(WaitingQueueError.NOT_ACTIVATE_STATUS);
        }
    }

    public void ensureTokenIsExpired(LocalDateTime now, long timeToLive) {
        if (tokenStatus == TokenStatus.EXPIRED || (expiresAt != null && now.isAfter(expiresAt.plusMinutes(timeToLive)))) {
            throw DomainException.create(WaitingQueueError.TOKEN_EXPIRED);
        }
    }

    public void validateTokenIsActiveAndNotExpired(LocalDateTime now, long timeToLive) {
        ensureTokenStatusIsActive();
        ensureTokenIsExpired(now, timeToLive);
    }
}

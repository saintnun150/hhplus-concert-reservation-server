package org.lowell.apps.waitingqueue.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.concert.domain.ConcertPolicy;
import org.lowell.apps.waitingqueue.domain.exception.WaitingQueueError;

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

    public boolean isActivateToken(LocalDateTime now) {
        if (tokenStatus == TokenStatus.ACTIVATE) {
            isExpiredTokenStatus(now);
            return true;
        }
        return false;
    }

    public void isActiveTokenStatus() {
        if (tokenStatus != TokenStatus.ACTIVATE) {
            throw DomainException.create(WaitingQueueError.NOT_ACTIVATE_STATUS);
        }
    }

    public void isExpiredTokenStatus(LocalDateTime now) {
        if (expiresAt != null && now.isAfter(expiresAt.plusMinutes(ConcertPolicy.EXPIRED_QUEUE_MINUTES))) {
            throw DomainException.create(WaitingQueueError.TOKEN_EXPIRED);
        }
    }

    public void checkActivateToken(LocalDateTime now) {
        isActiveTokenStatus();
        isExpiredTokenStatus(now);
    }

    public static WaitingQueueTokenInfo of(String token, TokenStatus tokenStatus, LocalDateTime expiresAt) {
        return new WaitingQueueTokenInfo(token, tokenStatus, expiresAt);
    }
}

package org.lowell.apps.domain.waitingqueue.unit.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.waitingqueue.domain.exception.WaitingQueueError;
import org.lowell.apps.waitingqueue.domain.model.TokenStatus;
import org.lowell.apps.waitingqueue.domain.model.WaitingQueueTokenInfo;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WaitingQueueTokenTest {

    @DisplayName("토큰 상태가 WAITING이 아닌 경우 예외 발생")
    @Test
    void throwException_when_TokenStatusIsNotWaiting() {
        WaitingQueueTokenInfo queueTokenInfo = new WaitingQueueTokenInfo("token", TokenStatus.ACTIVATE, LocalDateTime.now());
        assertThatThrownBy(queueTokenInfo::validateWaitingStatus)
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueError.NOT_WAITING_STATUS, e.getDomainError());
                });
    }

    @DisplayName("토큰 상태가 ACTIVATE가 아닌 경우 예외 발생")
    @Test
    void throwException_when_TokenStatusIsNotActivate() {
        WaitingQueueTokenInfo queueTokenInfo = new WaitingQueueTokenInfo("token", TokenStatus.WAITING, LocalDateTime.now());
        assertThatThrownBy(queueTokenInfo::isActiveTokenStatus)
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueError.NOT_ACTIVATE_STATUS, e.getDomainError());
                });
    }

    @DisplayName("토큰 만료시간이 다 된 경우 예외 발생")
    @Test
    void throwException_when_TokenExpiresAtIsExpired() {
        WaitingQueueTokenInfo queueTokenInfo = new WaitingQueueTokenInfo("token", TokenStatus.ACTIVATE, LocalDateTime.now().minusMinutes(10));

        assertThatThrownBy(() -> queueTokenInfo.checkActivateToken(LocalDateTime.now()))
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueError.TOKEN_EXPIRED, e.getDomainError());
                });
    }

}
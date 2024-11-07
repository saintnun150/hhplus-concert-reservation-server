package org.lowell.concert.domain.waitingqueue.unit.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueError;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueToken;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WaitingQueueTokenTest {

    @DisplayName("token 값이 없으면 예외가 발생한다.")
    @Test
    void throwException_when_TokenIsNull() {
        assertThatThrownBy(() -> WaitingQueueToken.builder()
                                                  .tokenId(1L)
                                                  .token(null)
                                                  .tokenStatus(TokenStatus.WAITING)
                                                  .createdAt(LocalDateTime.now())
                                                  .updatedAt(null)
                                                  .expiresAt(null)
                                                  .build())
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueError.INVALID_TOKEN_INPUT, e.getDomainError());
                });
    }

    @DisplayName("토큰 상태가 없거나 유효하지 않은 경우 예외 발생")
    @Test
    void throwException_when_TokenStatusIsNull() {
        assertThatThrownBy(() -> WaitingQueueToken.builder()
                                                  .tokenId(1L)
                                                  .token("token")
                                                  .tokenStatus(null)
                                                  .createdAt(LocalDateTime.now())
                                                  .updatedAt(null)
                                                  .expiresAt(null)
                                                  .build())
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueError.INVALID_TOKEN_STATUS_INPUT, e.getDomainError());
                });
    }

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
        assertThatThrownBy(queueTokenInfo::ensureTokenStatusIsActive)
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueError.NOT_ACTIVATE_STATUS, e.getDomainError());
                });
    }

    @DisplayName("토큰 만료시간이 다 된 경우 예외 발생")
    @Test
    void throwException_when_TokenExpiresAtIsExpired() {
        WaitingQueueTokenInfo queueTokenInfo = new WaitingQueueTokenInfo("token", TokenStatus.ACTIVATE, LocalDateTime.now().minusMinutes(10));

        assertThatThrownBy(() -> queueTokenInfo.validateTokenIsActiveAndNotExpired(LocalDateTime.now(), 10))
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueError.TOKEN_EXPIRED, e.getDomainError());
                });
    }

}
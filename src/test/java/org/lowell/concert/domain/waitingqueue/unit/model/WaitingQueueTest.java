package org.lowell.concert.domain.waitingqueue.unit.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueErrorCode;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WaitingQueueTest {

    @DisplayName("token 값이 없으면 예외가 발생한다.")
    @Test
    void throwException_when_TokenIsNull() {
        assertThatThrownBy(() -> WaitingQueue.builder()
                                             .tokenId(1L)
                                             .token(null)
                                             .tokenStatus(TokenStatus.WAITING)
                                             .createdAt(LocalDateTime.now())
                                             .updatedAt(null)
                                             .expiresAt(null)
                                             .build())
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueErrorCode.INVALID_TOKEN_INPUT, e.getErrorCode());
                });
    }

    @DisplayName("토큰 상태가 없거나 유효하지 않은 경우 예외 발생")
    @Test
    void throwException_when_TokenStatusIsNull() {
        assertThatThrownBy(() -> WaitingQueue.builder()
                                             .tokenId(1L)
                                             .token("token")
                                             .tokenStatus(null)
                                             .createdAt(LocalDateTime.now())
                                             .updatedAt(null)
                                             .expiresAt(null)
                                             .build())
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueErrorCode.INVALID_TOKEN_STATUS_INPUT, e.getErrorCode());
                });
    }

    @DisplayName("토큰 상태가 WAITING이 아닌 경우 예외 발생")
    @Test
    void throwException_when_TokenStatusIsNotWaiting() {
        WaitingQueue queue = WaitingQueue.builder()
                                         .tokenId(1L)
                                         .token("token")
                                         .tokenStatus(TokenStatus.ACTIVATE)
                                         .createdAt(LocalDateTime.now())
                                         .updatedAt(null)
                                         .expiresAt(LocalDateTime.now().plusMinutes(10))
                                         .build();

        assertThatThrownBy(queue::validateWaitingStatus)
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueErrorCode.NOT_WAITING_STATUS, e.getErrorCode());
                });
    }

    @DisplayName("토큰 상태가 ACTIVATE가 아닌 경우 예외 발생")
    @Test
    void throwException_when_TokenStatusIsNotActivate() {
        WaitingQueue queue = WaitingQueue.builder()
                                         .tokenId(1L)
                                         .token("token")
                                         .tokenStatus(TokenStatus.WAITING)
                                         .createdAt(LocalDateTime.now())
                                         .updatedAt(null)
                                         .expiresAt(null)
                                         .build();

        assertThatThrownBy(queue::validateActivateStatus)
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueErrorCode.NOT_ACTIVATE_STATUS, e.getErrorCode());
                });
    }

    @DisplayName("토큰 만료시간이 다 된 경우 예외 발생")
    @Test
    void throwException_when_TokenExpiresAtIsExpired() {
        WaitingQueue queue = WaitingQueue.builder()
                                         .tokenId(1L)
                                         .token("token")
                                         .tokenStatus(TokenStatus.ACTIVATE)
                                         .createdAt(LocalDateTime.now())
                                         .updatedAt(null)
                                         .expiresAt(LocalDateTime.now().minusMinutes(10))
                                         .build();

        assertThatThrownBy(() -> queue.validateTokenExpiredDate(10))
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertEquals(WaitingQueueErrorCode.TOKEN_EXPIRED, e.getErrorCode());
                });
    }

}
package org.lowell.concert.domain.waitingqueue.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueTokenErrorCode;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueTokenException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class WaitingQueueTokenInfoTest {

    @DisplayName("대기 상태의 토큰이 아닐 경우 예외가 발생한다.")
    @Test
    void throwException_when_exceed_deactivated_token_cnt() {
        WaitingQueueTokenInfo tokenInfo = WaitingQueueTokenInfo.builder()
                                                               .tokenId(1L)
                                                               .token("tokenUUID")
                                                               .tokenStatus(TokenStatus.EXPIRED)
                                                               .createdAt(LocalDateTime.now())
                                                               .build();

        assertThatThrownBy(tokenInfo::validateForWaiting)
                .isInstanceOfSatisfying(WaitingQueueTokenException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(WaitingQueueTokenErrorCode.ILLEGAL_TOKEN_STATUS);
                });
    }

}
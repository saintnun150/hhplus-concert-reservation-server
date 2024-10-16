package org.lowell.concert.domain.waitingqueue.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueTokenCommand;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueTokenErrorCode;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueTokenException;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueTokenRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WaitingQueueTokenServiceTest {

    @Mock
    private WaitingQueueTokenRepository tokenRepository;

    @InjectMocks
    private WaitingQueueTokenService tokenService;

    @DisplayName("토큰 생성을 위한 상태값 결정 시 현재 활성화된 토큰 수가 제한량보다 작을 경우 ACTIVATE 토큰 상태값을 반환한다.")
    @Test
    void tokenStatusIsWaiting_when_current_activatedToken_less_then_capacity() {
        int expectedActivatedCnt = 10;
        when(tokenRepository.getTokenCountByStatus(TokenStatus.ACTIVATE))
                .thenReturn(expectedActivatedCnt);

        TokenStatus creatingTokenStatus = tokenService.getCreatingTokenStatus();

        assertThat(creatingTokenStatus).isEqualTo(TokenStatus.ACTIVATE);
    }

    @DisplayName("토큰 생성을 위한 상태값 결정 시 현재 활성화된 토큰 수가 초과되면 WAITING 토큰 상태값을 반환한다.")
    @Test
    void tokenStatusIsWaiting_when_exceed_activated_token_cnt() {
        int maxCapacity = 50;
        when(tokenRepository.getTokenCountByStatus(TokenStatus.ACTIVATE))
                .thenReturn(maxCapacity);

        TokenStatus creatingTokenStatus = tokenService.getCreatingTokenStatus();

        assertThat(creatingTokenStatus).isEqualTo(TokenStatus.WAITING);
    }

    @DisplayName("대기열 순번을 체크할 때 토큰 조회시 없는 토큰일 경우 예외가 발생한다.")
    @Test
    void throwException_when_not_fount_token() {
        String token = "token1";
        when(tokenRepository.getQueueToken(token)).thenReturn(null);
        assertThatThrownBy(() -> tokenService.getTokenOrder(token))
                .isInstanceOfSatisfying(WaitingQueueTokenException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(WaitingQueueTokenErrorCode.NOT_FOUND_TOKEN);
                });
    }

    @DisplayName("활성 가능한 수만큼 대기상태인 토큰을 조회하였을 때 대기 상태의 토큰이 없으면 예외가 발생한다.")
    @Test
    void throwException_when_not_fount_waiting_status_token() {
        when(tokenRepository.getTokensByTokenStatusWithLimit(null))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> tokenService.getTokenIdsByTokenStatusWithLimit(null))
                .isInstanceOfSatisfying(WaitingQueueTokenException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(WaitingQueueTokenErrorCode.EMPTY_TOKENS);
                });
    }

    @DisplayName("대기 상태인 토큰들을 활성화 시킬 때 토큰 PK 목록이 없을 경우 예외가 발생한다.")
    @Test
    void throwException_when_not_found_waiting_status_token_ids() {
        WaitingQueueTokenCommand.Update command = new WaitingQueueTokenCommand.Update(null,
                                                                                      TokenStatus.ACTIVATE,
                                                                                      LocalDateTime.now(),
                                                                                      LocalDateTime.now().plusMinutes(5));
        assertThatThrownBy(() -> tokenService.updateToken(command))
                .isInstanceOfSatisfying(WaitingQueueTokenException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(WaitingQueueTokenErrorCode.EMPTY_TOKEN_IDS);
                });
    }

    @DisplayName("대기 상태인 토큰들을 활성화 시킬 때 잘못된 만료예정시간이 들어갈 경우 예외가 발생한다.")
    @Test
    void throwException_when_invalid_expires_date() {
        WaitingQueueTokenCommand.Update command = new WaitingQueueTokenCommand.Update(List.of(1L, 2L, 3L),
                                                                                      TokenStatus.ACTIVATE,
                                                                                      LocalDateTime.now(),
                                                                                      LocalDateTime.now().minusMinutes(10));
        assertThatThrownBy(() -> tokenService.updateToken(command))
                .isInstanceOfSatisfying(WaitingQueueTokenException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(WaitingQueueTokenErrorCode.INVALID_TOKEN_EXPIRES_DATE);
                });
    }


}
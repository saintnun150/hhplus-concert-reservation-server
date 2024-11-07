package org.lowell.concert.domain.waitingqueue.unit.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.application.waitingqueue.WaitingQueueInfo;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueError;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueToken;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueRepository;
import org.lowell.concert.domain.waitingqueue.service.WaitingQueueService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WaitingQueueTokenServiceTest {

    @Mock
    private WaitingQueueRepository waitingQueueRepository;

    @InjectMocks
    private WaitingQueueService waitingQueueService;


    @DisplayName("대기열 생성 테스트")
    @Test
    void createWaitingQueue() {
        // given
        String token = "token";
        TokenStatus status = TokenStatus.WAITING;
        WaitingQueueCommand.CreateToken createToken = new WaitingQueueCommand.CreateToken(token, status, null);

        when(waitingQueueRepository.createQueueToken(createToken))
                .thenReturn(new WaitingQueueTokenInfo(token, status, LocalDateTime.now()));
        // when
        WaitingQueueTokenInfo waitingQueueToken = waitingQueueService.createQueueToken(createToken);
        // then
        assertThat(waitingQueueToken.getToken()).isEqualTo(token);
    }

    @DisplayName("대기열 조회 시 일치하는 값이 없으면 예외가 발생한다.")
    @Test
    void getWaitingQueue() {
        // given
        String token = "token";
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken(token);

        when(waitingQueueRepository.findQueueToken(query))
                .thenThrow(DomainException.create(WaitingQueueError.NOT_FOUND_TOKEN));
        // then
        Assertions.assertThatThrownBy(() -> waitingQueueService.getQueueToken(query))
                  .isInstanceOfSatisfying(DomainException.class, e -> {
                      assertThat(e.getDomainError()).isEqualTo(WaitingQueueError.NOT_FOUND_TOKEN);
                  });
    }

    @DisplayName("대기열 순번 조회 시 상태가 이미 활성화된 상태일 경우 0순번을 반환한다.")
    @Test
    void test() {
        // given
        TokenStatus status = TokenStatus.ACTIVATE;
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken("token");
        WaitingQueueTokenInfo queueTokenInfo = new WaitingQueueTokenInfo("token", status, LocalDateTime.now().plusMinutes(5));

        // when
        when(waitingQueueRepository.findQueueToken(query)).thenReturn(Optional.of(queueTokenInfo));

        // then
        WaitingQueueInfo.Get queueTokenOrder = waitingQueueService.getQueueTokenOrder(query);
        assertThat(queueTokenOrder.getOrder()).isEqualTo(0L);
    }

    @DisplayName("대기열 순번 조회 시 만료상태일 경우 예외가 발생한다.")
    @Test
    void throwExceptionWhenActiveTokenIsExpired() {
        // given
        TokenStatus status = TokenStatus.ACTIVATE;
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken("token");
        WaitingQueueTokenInfo queueTokenInfo = new WaitingQueueTokenInfo("token", status, LocalDateTime.now().minusMinutes(10));

        when(waitingQueueRepository.findQueueToken(query)).thenReturn(Optional.of(queueTokenInfo));

        // then
        Assertions.assertThatThrownBy(() -> waitingQueueService.getQueueTokenOrder(query))
                  .isInstanceOfSatisfying(DomainException.class, e -> {
                      assertThat(e.getDomainError()).isEqualTo(WaitingQueueError.TOKEN_EXPIRED);
                  });
    }

    @DisplayName("대기열 순번 조회 시 대기열에도 없고 토큰 상태가 만료상태면 예외가 발생한다.")
    @Test
    void throwExceptionWhenOrderIsNull() {
        // given
        TokenStatus status = TokenStatus.EXPIRED;
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken("token");
        WaitingQueueTokenInfo queueTokenInfo = new WaitingQueueTokenInfo("token", status, LocalDateTime.now().minusMinutes(10));

        when(waitingQueueRepository.findQueueToken(query)).thenReturn(Optional.of(queueTokenInfo));
        when(waitingQueueRepository.findWaitingTokenOrder(new WaitingQueueQuery.GetOrder(queueTokenInfo.getToken(), TokenStatus.WAITING)))
                .thenReturn(null);

        // then
        Assertions.assertThatThrownBy(() -> waitingQueueService.getQueueTokenOrder(query))
                  .isInstanceOfSatisfying(DomainException.class, e -> {
                      assertThat(e.getDomainError()).isEqualTo(WaitingQueueError.NOT_WAITING_STATUS);
                  });
    }

    @DisplayName("대기열 활성화 큐 생성 가능 여부 테스트")
    @Test
    void hasCapacityForActiveToken() {
        // given
        when(waitingQueueRepository.findActivateQueueTokenCount(TokenStatus.ACTIVATE))
                .thenReturn(1L);
        // when
        boolean result = waitingQueueService.hasCapacityForActiveToken();
        // then
        assertThat(result).isTrue();
    }

    @DisplayName("상태에 따른 대기열 조회 테스트")
    @Test
    void getQueueTokensByStatusAndSize() {
        // given
        TokenStatus status = TokenStatus.WAITING;
        WaitingQueueQuery.GetQueues query = new WaitingQueueQuery.GetQueues(status, 1);

        when(waitingQueueRepository.findQueuesByStatusAndSize(query))
                .thenReturn(List.of(WaitingQueueToken.builder()
                                                     .tokenId(1L)
                                                     .token("token")
                                                     .tokenStatus(status)
                                                     .createdAt(LocalDateTime.now())
                                                     .updatedAt(null)
                                                     .expiresAt(null)
                                                     .build()));
        // when
        List<WaitingQueueToken> waitingQueueTokens = waitingQueueService.getQueueTokensByStatusAndSize(query);
        // then
        assertThat(waitingQueueTokens).hasSize(1);
    }

}
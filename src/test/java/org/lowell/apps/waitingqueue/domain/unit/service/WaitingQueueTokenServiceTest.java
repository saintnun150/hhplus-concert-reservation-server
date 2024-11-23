package org.lowell.apps.waitingqueue.domain.unit.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.apps.waitingqueue.application.WaitingQueueInfo;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueCommand;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueQuery;
import org.lowell.apps.waitingqueue.domain.exception.WaitingQueueError;
import org.lowell.apps.waitingqueue.domain.model.TokenStatus;
import org.lowell.apps.waitingqueue.domain.model.WaitingQueueTokenInfo;
import org.lowell.apps.waitingqueue.domain.repository.WaitingQueueRepository;
import org.lowell.apps.waitingqueue.domain.service.WaitingQueueService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
        WaitingQueueCommand.Create create = new WaitingQueueCommand.Create(token);

        when(waitingQueueRepository.createQueueToken(create))
                .thenReturn(new WaitingQueueTokenInfo(token, status, LocalDateTime.now()));
        // when
        WaitingQueueTokenInfo waitingQueueToken = waitingQueueService.createQueueToken(create);
        // then
        assertThat(waitingQueueToken.getToken()).isEqualTo(token);
    }

    @DisplayName("대기열 조회 시 일치하는 값이 없으면 예외가 발생한다.")
    @Test
    void getWaitingQueue() {
        // given
        String token = "token";
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken(token);

        when(waitingQueueRepository.findWaitingQueueToken(query))
                .thenThrow(DomainException.create(WaitingQueueError.NOT_FOUND_TOKEN));
        // then
        Assertions.assertThatThrownBy(() -> waitingQueueService.getWaitingQueueToken(query))
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
        when(waitingQueueRepository.findWaitingTokenOrder(query)).thenReturn(null);
        when(waitingQueueRepository.findActivateQueueToken(query)).thenReturn(Optional.of(queueTokenInfo));

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

        when(waitingQueueRepository.findWaitingTokenOrder(query)).thenReturn(null);
        when(waitingQueueRepository.findActivateQueueToken(query)).thenReturn(Optional.of(queueTokenInfo));

        // then
        Assertions.assertThatThrownBy(() -> waitingQueueService.getQueueTokenOrder(query))
                  .isInstanceOfSatisfying(DomainException.class, e -> {
                      assertThat(e.getDomainError()).isEqualTo(WaitingQueueError.TOKEN_EXPIRED);
                  });
    }

    @DisplayName("대기열 순번 조회 시 대기열에도 없고 참가열에도 없으면 예외가 발생한다.")
    @Test
    void throwExceptionWhenOrderIsNull() {
        // given
        TokenStatus status = TokenStatus.EXPIRED;
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken("token");
        WaitingQueueTokenInfo queueTokenInfo = new WaitingQueueTokenInfo("token", status, LocalDateTime.now().minusMinutes(10));

        when(waitingQueueRepository.findWaitingTokenOrder(new WaitingQueueQuery.GetToken(queueTokenInfo.getToken())))
                .thenReturn(null);
        when(waitingQueueRepository.findActivateQueueToken(query)).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> waitingQueueService.getQueueTokenOrder(query))
                  .isInstanceOfSatisfying(DomainException.class, e -> {
                      assertThat(e.getDomainError()).isEqualTo(WaitingQueueError.NOT_FOUND_TOKEN);
                  });
    }

    @DisplayName("특정 시간간격 T마다 N명의 사람을 입장시킨다고 할 때 주어진 token 순번 WO에 대한 남은 시간은 ((WO - 1) / N) * T이다.")
    @Test
    void calculateWaitingTimeSeconds() {
        // given
        Long order = 50L;
        Long intervalTime = 10L;
        Long activateCount = 2L;

        // when
        long result = waitingQueueService.calculateWaitingTimeSeconds(order, intervalTime, TimeUnit.SECONDS, activateCount);
        // then
        assertThat(result).isEqualTo(240L);
    }

}
package org.lowell.concert.domain.waitingqueue.unit.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueErrorCode;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;
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
class WaitingQueueServiceTest {

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
        WaitingQueueCommand.Create create = new WaitingQueueCommand.Create(token, status, null);

        when(waitingQueueRepository.createWaitingQueue(create))
                .thenReturn(WaitingQueue.builder()
                                        .tokenId(1L)
                                        .token(token)
                                        .tokenStatus(status)
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(null)
                                        .expiresAt(null)
                                        .build());
        // when
        WaitingQueue waitingQueue = waitingQueueService.createWaitingQueue(create);
        // then
        assertThat(waitingQueue.getTokenId()).isEqualTo(1L);
        assertThat(waitingQueue.getToken()).isEqualTo(token);
    }

    @DisplayName("대기열 조회 시 일치하는 값이 없으면 예외가 발생한다.")
    @Test
    void getWaitingQueue() {
        // given
        String token = "token";
        WaitingQueueQuery.GetQueue query = new WaitingQueueQuery.GetQueue(token);

        when(waitingQueueRepository.getWaitingQueue(query))
                .thenThrow(DomainException.create(WaitingQueueErrorCode.NOT_FOUND_TOKEN));
        // then
        Assertions.assertThatThrownBy(() -> waitingQueueService.getWaitingQueue(query))
                  .isInstanceOfSatisfying(DomainException.class, e -> {
                      assertThat(e.getErrorCode()).isEqualTo(WaitingQueueErrorCode.NOT_FOUND_TOKEN);
                  });
    }

    @DisplayName("대기열 순번 조회 시 상태가 대기중이 아니면 예외가 발생한다.")
    @Test
    void throwExceptionWhenStatusIsNotWaiting() {
        // given
        TokenStatus status = TokenStatus.ACTIVATE;
        WaitingQueueQuery.GetQueue query = new WaitingQueueQuery.GetQueue("token");

        when(waitingQueueRepository.getWaitingQueue(query))
                .thenReturn(Optional.ofNullable(WaitingQueue.builder()
                                                            .tokenId(1L)
                                                            .token("token")
                                                            .tokenStatus(status)
                                                            .createdAt(LocalDateTime.now())
                                                            .updatedAt(null)
                                                            .expiresAt(null)
                                                            .build()));

        // then
        Assertions.assertThatThrownBy(() -> waitingQueueService.getWaitingQueueOrder(query))
                  .isInstanceOfSatisfying(DomainException.class, e -> {
                      assertThat(e.getErrorCode()).isEqualTo(WaitingQueueErrorCode.NOT_WAITING_STATUS);
                  });
    }

    @DisplayName("대기열 순번 조회 시 조회 값이 없다면 예외가 발생한다.")
    @Test
    void throwExceptionWhenOrderIsNull() {
        // given
        TokenStatus status = TokenStatus.WAITING;
        WaitingQueueQuery.GetQueue query = new WaitingQueueQuery.GetQueue("token");

        when(waitingQueueRepository.getWaitingQueue(query))
                .thenReturn(Optional.ofNullable(WaitingQueue.builder()
                                                            .tokenId(1L)
                                                            .token("token")
                                                            .tokenStatus(status)
                                                            .createdAt(LocalDateTime.now())
                                                            .updatedAt(null)
                                                            .expiresAt(null)
                                                            .build()));

        when(waitingQueueRepository.getWaitingOrder(new WaitingQueueQuery.Order(1L, status)))
                .thenReturn(null);

        // then
        Assertions.assertThatThrownBy(() -> waitingQueueService.getWaitingQueueOrder(query))
                  .isInstanceOfSatisfying(DomainException.class, e -> {
                      assertThat(e.getErrorCode()).isEqualTo(WaitingQueueErrorCode.INVALID_WAITING_ORDER);
                  });
    }

    @DisplayName("대기열 활성화 큐 생성 가능 여부 테스트")
    @Test
    void createActivationQueueImmediately() {
        // given
        when(waitingQueueRepository.getActivateQueueCount(TokenStatus.ACTIVATE))
                .thenReturn(1L);
        // when
        boolean result = waitingQueueService.createActivationQueueImmediately();
        // then
        assertThat(result).isTrue();
    }

    @DisplayName("상태에 따른 대기열 조회 테스트")
    @Test
    void getWaitingQueueByStatus() {
        // given
        TokenStatus status = TokenStatus.WAITING;
        WaitingQueueQuery.GetQueues query = new WaitingQueueQuery.GetQueues(status, 1);

        when(waitingQueueRepository.getWaitingQueues(query))
                .thenReturn(List.of(WaitingQueue.builder()
                                                .tokenId(1L)
                                                .token("token")
                                                .tokenStatus(status)
                                                .createdAt(LocalDateTime.now())
                                                .updatedAt(null)
                                                .expiresAt(null)
                                                .build()));
        // when
        List<WaitingQueue> waitingQueues = waitingQueueService.getWaitingQueueByStatus(query);
        // then
        assertThat(waitingQueues).hasSize(1);
    }

}
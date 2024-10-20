package org.lowell.concert.domain.waitingqueue.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueErrorCode;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueException;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;
import org.lowell.concert.domain.waitingqueue.service.WaitingQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class WaitingQueueIntegrationTest {

    @Autowired
    private WaitingQueueService waitingQueueService;

    @DisplayName("대기열 생성 테스트")
    @Test
    void createWaitingQueue() {
        // given
        String token = UUID.randomUUID().toString();
        WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token, TokenStatus.WAITING);
        WaitingQueue waitingQueue = waitingQueueService.createWaitingQueue(command);
        assertThat(waitingQueue.getToken()).isEqualTo(token);

    }

    @DisplayName("주어진 token 값에 대한 대기열 조회")
    @Test
    void getWaitingQueue() {
        // given
        String token = UUID.randomUUID().toString();
        WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token, TokenStatus.WAITING);
        WaitingQueue waitingQueue = waitingQueueService.createWaitingQueue(command);
        // when
        WaitingQueueQuery.GetQueue query = new WaitingQueueQuery.GetQueue(token);
        WaitingQueue getWaitingQueue = waitingQueueService.getWaitingQueue(query);
        // then
        assertThat(getWaitingQueue.getToken()).isEqualTo(token);
    }

    @DisplayName("주어진 token 값에 대한 대기열 조회시 값이 없으면 예외가 발생")
    @Test
    void throwException_when_getWaitingQueue() {
        // given
        String token = UUID.randomUUID().toString();
        String token2 = UUID.randomUUID().toString();
        WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token, TokenStatus.WAITING);
        WaitingQueue waitingQueue = waitingQueueService.createWaitingQueue(command);
        // when
        WaitingQueueQuery.GetQueue query = new WaitingQueueQuery.GetQueue(token2);
        // then
        assertThatThrownBy(() -> waitingQueueService.getWaitingQueue(query))
                .isInstanceOfSatisfying(WaitingQueueException.class, e -> {
                    assertThat(e.getErrorCode()).isEqualTo(WaitingQueueErrorCode.NOT_FOUND_TOKEN);
                });
    }

    @DisplayName("대기열 생성 후 대기열 순번 조회 시 상태가 대기중이 아니라면 예외 발생")
    @Test
    void throwException_when_getWaitingQueueStatus() {
        // given
        String token = UUID.randomUUID().toString();
        WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token, TokenStatus.ACTIVATE);
        waitingQueueService.createWaitingQueue(command);
        // when
        WaitingQueueQuery.GetQueue query = new WaitingQueueQuery.GetQueue(token);
        // then
        assertThatThrownBy(() -> waitingQueueService.getWaitingQueueOrder(query))
                .isInstanceOfSatisfying(WaitingQueueException.class, e -> {
                    assertThat(e.getErrorCode()).isEqualTo(WaitingQueueErrorCode.NOT_WAITING_STATUS);
                });
    }

    @DisplayName("대기열 순번 조회시 일치하는 값이 있으면 순번을 반환한다.")
    @Test
    void getWaitingQueueOrder() {
        // given
        for (int i = 0; i < 120; i++) {
            String token = "token" + i;
            boolean isCreatingActiveQueue = waitingQueueService.createActivationQueueImmediately();
            WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token, isCreatingActiveQueue ? TokenStatus.ACTIVATE : TokenStatus.WAITING);
            waitingQueueService.createWaitingQueue(command);
        }

        String token = "token120";
        WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token, TokenStatus.WAITING);
        waitingQueueService.createWaitingQueue(command);

        // when

        WaitingQueueQuery.GetQueue query = new WaitingQueueQuery.GetQueue(token);
        Long waitingQueueOrder = waitingQueueService.getWaitingQueueOrder(query);

        assertThat(waitingQueueOrder).isEqualTo(71L);
    }

    @DisplayName("대기열이 만료될 경우 만료 상태로 변경해줘야한다.")
    @Test
    void updateWaitingQueueWhenExpired() {
        // given
        String token = UUID.randomUUID().toString();
        WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token, TokenStatus.ACTIVATE);
        WaitingQueue waitingQueue = waitingQueueService.createWaitingQueue(command);
        // when
        WaitingQueueCommand.Update update = new WaitingQueueCommand.Update(waitingQueue.getTokenId(),
                                                                           TokenStatus.EXPIRED,
                                                                           LocalDateTime.now());
        waitingQueueService.updateWaitingQueue(update);
        // then
        WaitingQueue updatedWaitingQueue = waitingQueueService.getWaitingQueue(new WaitingQueueQuery.GetQueue(token));
        assertThat(updatedWaitingQueue.getTokenStatus()).isEqualTo(TokenStatus.EXPIRED);
    }

    @DisplayName("대기열에 있는 대기중인 토큰을 일정 시간마다 체크해 ACTIVATE로 변경해야한다.")
    @Test
    void updateWaitingQueueWhenChangeActivateStatusBySchedule() throws InterruptedException {
        // given
        for (int i = 0; i < 70; i++) {
            String token = "token" + i;
            boolean isCreatingActiveQueue = waitingQueueService.createActivationQueueImmediately();
            WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token, isCreatingActiveQueue ? TokenStatus.ACTIVATE : TokenStatus.WAITING);
            waitingQueueService.createWaitingQueue(command);
        }


        // 10초에 한 번 씩 총 두 번 3개의 대기열을 activate로 변경

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        for (int j = 0; j < 2; j++) {
            scheduler.schedule(() -> {
                List<Long> tokenIds = waitingQueueService.getWaitingQueueByStatus(new WaitingQueueQuery.GetQueues(TokenStatus.WAITING, 3))
                                                         .stream()
                                                         .map(WaitingQueue::getTokenId)
                                                         .toList();
                waitingQueueService.updateWaitingQueues(new WaitingQueueCommand.UpdateBatch(tokenIds,
                                                                                            TokenStatus.ACTIVATE,
                                                                                            LocalDateTime.now(),
                                                                                            LocalDateTime.now().plusMinutes(20)));
            }, j * 10, TimeUnit.SECONDS);
        }

        Thread.sleep(30000);

        List<WaitingQueue> waitingQueues = waitingQueueService.getWaitingQueueByStatus(new WaitingQueueQuery.GetQueues(TokenStatus.ACTIVATE, 100));
        assertThat(waitingQueues.size()).isEqualTo(56);

    }




}

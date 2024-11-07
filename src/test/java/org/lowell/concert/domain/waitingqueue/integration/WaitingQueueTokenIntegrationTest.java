package org.lowell.concert.domain.waitingqueue.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.application.support.DatabaseCleanUp;
import org.lowell.concert.application.waitingqueue.WaitingQueueInfo;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueError;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
import org.lowell.concert.domain.waitingqueue.service.WaitingQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class WaitingQueueTokenIntegrationTest {
    @Autowired
    private WaitingQueueService waitingQueueService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("대기열 생성 테스트")
    @Test
    void createWaitingQueue() {
        // given
        String token = UUID.randomUUID().toString();
        WaitingQueueCommand.CreateToken command = new WaitingQueueCommand.CreateToken(token, TokenStatus.WAITING, null);
        WaitingQueueTokenInfo waitingQueueTokenInfo = waitingQueueService.createQueueToken(command);
        assertThat(waitingQueueTokenInfo.getToken()).isEqualTo(token);

    }

    @DisplayName("주어진 token 값에 대한 대기열 조회")
    @Test
    void getWaitingQueue() {
        // given
        String token = UUID.randomUUID().toString();
        WaitingQueueCommand.CreateToken command = new WaitingQueueCommand.CreateToken(token, TokenStatus.WAITING, null);
        waitingQueueService.createQueueToken(command);
        // when
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken(token);
        WaitingQueueTokenInfo getTokenInfo = waitingQueueService.getQueueToken(query);
        // then
        assertThat(getTokenInfo.getToken()).isEqualTo(token);
    }

    @DisplayName("주어진 token 값에 대한 대기열 조회시 값이 없으면 예외가 발생")
    @Test
    void throwException_when_getWaitingQueue() {
        // given
        String token = UUID.randomUUID().toString();
        String token2 = UUID.randomUUID().toString();
        WaitingQueueCommand.CreateToken command = new WaitingQueueCommand.CreateToken(token, TokenStatus.WAITING, null);
        waitingQueueService.createQueueToken(command);
        // when
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken(token2);
        // then
        assertThatThrownBy(() -> waitingQueueService.getQueueToken(query))
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertThat(e.getDomainError()).isEqualTo(WaitingQueueError.NOT_FOUND_TOKEN);
                });
    }

    @DisplayName("대기열 생성 후 대기열 순번 조회 시 상태가 이미 활성되 상태면 0 순번을 반환한다.")
    @Test
    void throwOrderWhenTokenIsAlreadyActive() {
        // given
        String token = UUID.randomUUID().toString();
        WaitingQueueCommand.CreateToken command = new WaitingQueueCommand.CreateToken(token, TokenStatus.ACTIVATE, LocalDateTime.now().plusMinutes(5));
        waitingQueueService.createQueueToken(command);
        // when
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken(token);
        // then
        WaitingQueueInfo.Get queueTokenOrder = waitingQueueService.getQueueTokenOrder(query);
        assertThat(queueTokenOrder.getOrder()).isEqualTo(0L);
    }

    @DisplayName("대기열 순번 조회시 일치하는 값이 있으면 순번을 반환한다.")
    @Test
    void getWaitingQueueOrder() {
        // given
        for (int i = 0; i < 120; i++) {
            String token = "token" + i;
            boolean isCreatingActiveQueue = waitingQueueService.hasCapacityForActiveToken();
            WaitingQueueCommand.CreateToken command = new WaitingQueueCommand.CreateToken(token, isCreatingActiveQueue ? TokenStatus.ACTIVATE : TokenStatus.WAITING, null);
            waitingQueueService.createQueueToken(command);
        }

        String token = "token120";
        WaitingQueueCommand.CreateToken command = new WaitingQueueCommand.CreateToken(token, TokenStatus.WAITING, null);
        waitingQueueService.createQueueToken(command);

        // when

        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken(token);
        WaitingQueueInfo.Get queueTokenOrder = waitingQueueService.getQueueTokenOrder(query);
        assertThat(queueTokenOrder.getOrder()).isEqualTo(71L);
    }

//    @DisplayName("대기열에 있는 대기중인 토큰을 일정 시간마다 체크해 ACTIVATE로 변경해야한다.")
//    @Test
//    void updateWaitingQueueWhenChangeActivateStatusBySchedule() throws InterruptedException {
//        // given
//        for (int i = 0; i < 70; i++) {
//            String token = "token" + i;
//            boolean isCreatingActiveQueue = waitingQueueService.createActivationQueueImmediately();
//            WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token, isCreatingActiveQueue ? TokenStatus.ACTIVATE : TokenStatus.WAITING, null);
//            waitingQueueService.createWaitingQueue(command);
//        }
//
//        // 10초에 한 번 씩 총 두 번 3개의 대기열을 activate로 변경
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        for (int j = 0; j < 2; j++) {
//            scheduler.schedule(() -> {
//                List<Long> tokenIds = waitingQueueService.getWaitingQueueByStatus(new WaitingQueueQuery.GetQueues(TokenStatus.WAITING, 3))
//                                                         .stream()
//                                                         .map(WaitingQueue::getTokenId)
//                                                         .toList();
//                waitingQueueService.updateWaitingQueues(new WaitingQueueCommand.UpdateBatch(tokenIds,
//                                                                                            TokenStatus.ACTIVATE,
//                                                                                            LocalDateTime.now(),
//                                                                                            LocalDateTime.now().plusMinutes(20)));
//            }, j * 10, TimeUnit.SECONDS);
//        }
//
//        Thread.sleep(30000);
//
//        List<WaitingQueue> waitingQueues = waitingQueueService.getWaitingQueueByStatus(new WaitingQueueQuery.GetQueues(TokenStatus.ACTIVATE, 100));
//        assertThat(waitingQueues.size()).isEqualTo(56);
//
//    }
}

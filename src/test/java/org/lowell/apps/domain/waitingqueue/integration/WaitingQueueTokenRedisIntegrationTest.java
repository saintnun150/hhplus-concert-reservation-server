package org.lowell.apps.domain.waitingqueue.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.application.support.DatabaseCleanUp;
import org.lowell.apps.common.util.UUIDGenerator;
import org.lowell.apps.concert.domain.ConcertPolicy;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueCommand;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueQuery;
import org.lowell.apps.waitingqueue.domain.model.TokenStatus;
import org.lowell.apps.waitingqueue.domain.model.WaitingQueueTokenInfo;
import org.lowell.apps.waitingqueue.domain.service.WaitingQueueService;
import org.lowell.apps.waitingqueue.infra.repository.WaitingQueueRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class WaitingQueueTokenRedisIntegrationTest {

    @Autowired
    private WaitingQueueService waitingQueueService;

    @Autowired
    private WaitingQueueRepositoryImpl waitingQueueRedisRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("waiting-queue.type", () -> "redis");
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.cleanRedisData();
    }

    @DisplayName("주어진 token값에 대한 대기열이 ZSET 생성된다.")
    @Test
    void createWaitingQueue() {
        // given
        String token = UUIDGenerator.generateTimestampUUID();
        WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token);
        WaitingQueueTokenInfo waitingQueueTokenInfo = waitingQueueService.createQueueToken(command);
        assertThat(waitingQueueTokenInfo.getToken()).isEqualTo(token);
    }

    @DisplayName("주어진 token 값에 대한 대기열 조회")
    @Test
    void getWaitingQueue() {
        // given
        String token = UUIDGenerator.generateTimestampUUID();
        WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token);
        waitingQueueService.createQueueToken(command);
        // when
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken(token);
        WaitingQueueTokenInfo getTokenInfo = waitingQueueService.getWaitingQueueToken(query);
        // then
        assertThat(getTokenInfo.getToken()).isEqualTo(token);
        assertThat(getTokenInfo.getTokenStatus()).isEqualTo(TokenStatus.WAITING);
    }

    @DisplayName("대기열에 있는 토큰이 N개 일 때 M개 만큼 활성화되면 남은 대기열 토큰은 N-M개가 된다.")
    @Test
    void activateWaitingToken() {
        // given

        int waitingTokenSize = 100;
        long activateTokenSize = 40;
        for (int i = 0; i < waitingTokenSize; i++) {
            String token = UUIDGenerator.generateTimestampUUID();
            WaitingQueueCommand.Create command = new WaitingQueueCommand.Create(token);
            waitingQueueService.createQueueToken(command);
        }

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(ConcertPolicy.EXPIRED_QUEUE_MINUTES);

        // when
        waitingQueueRedisRepository.activateWaitingToken(new WaitingQueueQuery.GetQueues(40, expiresAt));
        // then
        Long tokenCount = waitingQueueRedisRepository.findTokenCount(TokenStatus.WAITING);
        assertThat(tokenCount).isEqualTo(waitingTokenSize - activateTokenSize);
    }


}

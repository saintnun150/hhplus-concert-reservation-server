package org.lowell.concert.domain.waitingqueue.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.application.support.DatabaseCleanUp;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.common.util.UUIDGenerator;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueError;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
import org.lowell.concert.domain.waitingqueue.service.WaitingQueueService;
import org.lowell.concert.infra.redis.waitingqueue.WaitingQueueRedisRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class WaitingQueueTokenRedisIntegrationTest {

    @Autowired
    private WaitingQueueService waitingQueueService;

    @Autowired
    private WaitingQueueRedisRepositoryImpl waitingQueueRedisRepository;

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
        WaitingQueueCommand.CreateToken command = new WaitingQueueCommand.CreateToken(token, TokenStatus.WAITING, null);
        WaitingQueueTokenInfo waitingQueueTokenInfo = waitingQueueService.createQueueToken(command);
        assertThat(waitingQueueTokenInfo.getToken()).isEqualTo(token);
    }

    @DisplayName("주어진 token 값에 대한 대기열 조회")
    @Test
    void getWaitingQueue() {
        // given
        String token = UUIDGenerator.generateTimestampUUID();
        WaitingQueueCommand.CreateToken command = new WaitingQueueCommand.CreateToken(token, TokenStatus.WAITING, null);
        waitingQueueService.createQueueToken(command);
        // when
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken(token);
        WaitingQueueTokenInfo getTokenInfo = waitingQueueService.getQueueToken(query);
        // then
        assertThat(getTokenInfo.getToken()).isEqualTo(token);
        assertThat(getTokenInfo.getTokenStatus()).isEqualTo(command.status());
    }

    @DisplayName("주어진 token 값에 대한 순서 조회 시 활성화 되었지만 이미 만료된 토큰일 경우 예외가 발생한다.")
    @Test
    void getWaitingQueueOrderWhenTokenIsExpired() {
        // given
        String token = UUIDGenerator.generateTimestampUUID();
        WaitingQueueCommand.CreateToken command = new WaitingQueueCommand.CreateToken(token, TokenStatus.ACTIVATE, LocalDateTime.now().minusMinutes(20L));
        waitingQueueService.createQueueToken(command);
        // when
        WaitingQueueQuery.GetToken query = new WaitingQueueQuery.GetToken(token);
        // then
        assertThatThrownBy(() -> waitingQueueService.getQueueTokenOrder(query))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(WaitingQueueError.TOKEN_EXPIRED);
                });
    }

    @DisplayName("대기열에 있는 토큰이 N개 일 때 M개 만큼 활성화되면 남은 대기열 토큰은 N-M개가 된다.")
    @Test
    void activateWaitingToken() {
        // given

        int waitingTokenSize = 100;
        long activateTokenSize = 40;
        for (int i = 0; i < waitingTokenSize; i++) {
            String token = UUIDGenerator.generateTimestampUUID();
            WaitingQueueCommand.CreateToken command = new WaitingQueueCommand.CreateToken(token, TokenStatus.WAITING, null);
            waitingQueueService.createQueueToken(command);
        }

        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(ConcertPolicy.EXPIRED_QUEUE_MINUTES);

        // when
        waitingQueueRedisRepository.activateWaitingToken(new WaitingQueueQuery.GetQueues(null, expiresAt, 40));
        // then
        Long tokenCount = waitingQueueRedisRepository.findTokenCount(TokenStatus.WAITING);
        assertThat(tokenCount).isEqualTo(waitingTokenSize - activateTokenSize);
    }


}

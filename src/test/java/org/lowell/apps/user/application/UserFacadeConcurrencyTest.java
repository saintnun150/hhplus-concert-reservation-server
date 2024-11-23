package org.lowell.apps.user.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.support.DatabaseCleanUp;
import org.lowell.apps.user.domain.model.User;
import org.lowell.apps.user.domain.model.UserAccount;
import org.lowell.apps.user.infra.repository.UserAccountJpaRepository;
import org.lowell.apps.user.infra.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class UserFacadeConcurrencyTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    UserAccountJpaRepository userAccountJpaRepository;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("비관적 락을 통해 포인트 충전을 10회 진행할 경우 10,000원이 충전되어야 한다.")
    @Test
    void balanceIs1000WhenMultipleChargingToSameAccount() throws InterruptedException {
        // given
        User test1 = userJpaRepository.save(User.builder()
                                                .username("test1")
                                                .build());
        userAccountJpaRepository.save(UserAccount.builder()
                                                 .userId(test1.getUserId())
                                                 .balance(0L)
                                                 .build());
        int threadCount = 10;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userFacade.chargeBalanceWithLock(test1.getUserId(), 1000L);
                    success.incrementAndGet();
                } catch (Exception e) {
                    log.error("## error : {}", e.getLocalizedMessage());
                    failed.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        userAccountJpaRepository.findByUserId(test1.getUserId())
                                .ifPresent(userAccount -> {
                                    assertThat(userAccount.getBalance()).isEqualTo(10000L);
                                });
        assertThat(success.get()).isEqualTo(10);
    }
}

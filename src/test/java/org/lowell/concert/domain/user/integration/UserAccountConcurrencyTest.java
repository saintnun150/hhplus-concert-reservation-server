package org.lowell.concert.domain.user.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.application.support.DatabaseCleanUp;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.common.support.LockRepository;
import org.lowell.concert.domain.user.dto.UserAccountCommand;
import org.lowell.concert.domain.user.model.UserAccount;
import org.lowell.concert.domain.user.service.UserAccountService;
import org.lowell.concert.infra.db.user.repository.UserAccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.lowell.concert.domain.user.exception.UserAccountError.EXCEED_BALANCE;

@SpringBootTest
public class UserAccountConcurrencyTest {

    @Autowired
    private UserAccountJpaRepository userAccountJpaRepository;

    @Autowired
    private UserAccountService accountService;

    @Autowired
    private LockRepository lockRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;
    @Autowired
    private UserAccountService userAccountService;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("포인트 사용 시 사용금액이 초과되면 예외가 발생한다.")
    @Test
    void throwExceptionWhenPointUsageExceedsBalance() throws InterruptedException {
        // given
        Long userId = 1L;
        long balance = 10;
        long usageAmount = 1;
        int useCount = 12;

        UserAccount account = userAccountJpaRepository.save(UserAccount.builder()
                                                                       .userId(userId)
                                                                       .balance(balance)
                                                                       .build());
        // when
        ExecutorService executorService = Executors.newFixedThreadPool(useCount);
        CountDownLatch latch = new CountDownLatch(useCount);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        for (int i = 0; i < useCount; i++) {
            executorService.submit(() -> {
                try {
                    accountService.useBalance(new UserAccountCommand.Action(userId, usageAmount));
                    success.incrementAndGet();
                } catch (Exception e) {
                    if (e instanceof DomainException) {
                        DomainException ex = (DomainException) e;
                        if (ex.getDomainError() == EXCEED_BALANCE) {
                            failed.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();
        // then
        assertThat(success.get()).isEqualTo(10);
        assertThat(failed.get()).isEqualTo(2);
        UserAccount userAccount = accountService.getUserAccount(userId);
        assertThat(userAccount.getBalance()).isEqualTo(balance - usageAmount * success.get());
    }

    @DisplayName("포인트 충전 메서드 시작과 끝에 Redis setNX를 이용해 락을 잡는 테스트")
    @Test
    void redisTest() throws InterruptedException {

        // given
        Long userId = 1L;
        long balance = 10000;
        long usageAmount = 10;
        int chargeCount = 10;

        UserAccount account = userAccountJpaRepository.save(UserAccount.builder()
                                                                       .userId(userId)
                                                                       .balance(balance)
                                                                       .build());

        String lockKey = String.valueOf(account.getAccountId());

        ExecutorService executorService = Executors.newFixedThreadPool(chargeCount);
        CountDownLatch latch = new CountDownLatch(chargeCount);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        for (int i = 0; i < chargeCount; i++) {
            executorService.submit(() -> {
                try {
                    Boolean lock = lockRepository.lock(lockKey, 2000L, TimeUnit.MILLISECONDS);
                    if (lock) {
                        userAccountService.chargeBalance(new UserAccountCommand.Action(account.getUserId(), usageAmount));
                    }

                    success.incrementAndGet();
                } catch (Exception e) {
                    failed.incrementAndGet();
                } finally {
                    lockRepository.unlock(lockKey);
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        userAccountJpaRepository.findByUserId(userId)
                                .ifPresent(userAccount -> {
                                    assertThat(userAccount.getBalance()).isEqualTo(10010L);
                                });
    }




}

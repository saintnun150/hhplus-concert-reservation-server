package org.lowell.apps.user.domain.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.support.DatabaseCleanUp;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.user.domain.dto.UserAccountCommand;
import org.lowell.apps.user.domain.model.UserAccount;
import org.lowell.apps.user.domain.service.UserAccountService;
import org.lowell.apps.user.infra.repository.UserAccountJpaRepository;
import org.lowell.apps.waitingqueue.infra.redis.RedisRLockManager;
import org.lowell.apps.waitingqueue.infra.redis.RedisSimpleLockManager;
import org.lowell.apps.waitingqueue.infra.redis.RedisSpinLockManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.lowell.apps.user.domain.exception.UserAccountError.EXCEED_BALANCE;

@Slf4j
@SpringBootTest
public class UserAccountConcurrencyTest {

    @Autowired
    private UserAccountJpaRepository userAccountJpaRepository;

    @Autowired
    private UserAccountService accountService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private RedisSimpleLockManager simpleLockRepository;

    @Autowired
    private RedisSpinLockManager spinLockRepository;

    @Autowired
    private RedisRLockManager rLockRepository;

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

    @DisplayName("낙관적 락을 사용해 포인트 충전을 1000번 할 경우 모두 성공한다.")
    @Test
    void attemptChargeWithOptimisticLock1000Times() throws InterruptedException {
        Long userId = 1L;
        long balance = 100;
        long chargeAmount = 1000;
        int chargeCount = 1000;

        UserAccount account = userAccountJpaRepository.save(UserAccount.builder()
                                                                       .userId(userId)
                                                                       .balance(balance)
                                                                       .build());
        ExecutorService executorService = Executors.newFixedThreadPool(chargeCount);
        CountDownLatch latch = new CountDownLatch(chargeCount);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        for (int i = 0; i < chargeCount; i++) {
            executorService.submit(() -> {
                int maxRetries = 100;
                int retryCount = 0;
                boolean isCharged = false;

                while (retryCount < maxRetries && !isCharged) {
                    try {
                        accountService.chargeBalance(new UserAccountCommand.Action(userId, chargeAmount));
                        success.incrementAndGet();
                        isCharged = true;
                    } catch (ObjectOptimisticLockingFailureException oe) {
                        retryCount++;
                        log.warn("## retry attempt:[{}]", retryCount);
                    } catch (Exception e) {
                        log.warn("## charge error:[{}]", e.getClass().getSimpleName(), e);
                        failed.incrementAndGet();
                        break;
                    }
                }
                latch.countDown();
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        assertThat(success.get()).isEqualTo(1000);
        UserAccount userAccount = accountService.getUserAccount(userId);
        assertThat(userAccount.getBalance()).isEqualTo(balance + chargeAmount * success.get());
    }

    @DisplayName("비관적 락을 사용해 포인트 충전을 1000번 할 경우 모두 성공한다.")
    @Test
    void attemptChargeWithPessimisticLock100Times() throws InterruptedException {
        Long userId = 1L;
        long balance = 100;
        long chargeAmount = 1000;
        int chargeCount = 1000;

        UserAccount account = userAccountJpaRepository.save(UserAccount.builder()
                                                                       .userId(userId)
                                                                       .balance(balance)
                                                                       .build());
        ExecutorService executorService = Executors.newFixedThreadPool(chargeCount);
        CountDownLatch latch = new CountDownLatch(chargeCount);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        for (int i = 0; i < chargeCount; i++) {
            executorService.submit(() -> {
                try {
                    accountService.chargeBalanceWithLock(new UserAccountCommand.Action(userId, chargeAmount));
                    success.incrementAndGet();
                } catch (Exception e) {
                    log.warn("## charge error:[{}]", e.getClass().getSimpleName(), e);
                    failed.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        assertThat(success.get()).isEqualTo(1000);
        UserAccount userAccount = accountService.getUserAccount(userId);
        assertThat(userAccount.getBalance()).isEqualTo(balance + chargeAmount * success.get());
    }

    @DisplayName("레디스 SpinLock을 사용해 포인트 충전을 1000번 할 경우 모두 성공한다.")
    @Test
    void attemptChargeWithRedisSpinLock1000Times() throws InterruptedException {
        Long userId = 1L;
        long balance = 100;
        long chargeAmount = 1000;
        int chargeCount = 1000;

        UserAccount account = userAccountJpaRepository.save(UserAccount.builder()
                                                                       .userId(userId)
                                                                       .balance(balance)
                                                                       .build());
        ExecutorService executorService = Executors.newFixedThreadPool(chargeCount);
        CountDownLatch latch = new CountDownLatch(chargeCount);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        for (int i = 0; i < chargeCount; i++) {
            executorService.submit(() -> {
                String lockKey = "user:account:" + userId;
                try {
                    Boolean lock = spinLockRepository.tryLock(lockKey, 500L, 200L, TimeUnit.MILLISECONDS);
                    if (lock) {
                        accountService.chargeBalance(new UserAccountCommand.Action(userId, chargeAmount));
                        success.incrementAndGet();
                    }
                } catch (Exception e) {
                    log.warn("## charge error:[{}]", e.getClass().getSimpleName(), e);
                    failed.incrementAndGet();
                } finally {
                    simpleLockRepository.unlock(lockKey);
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        assertThat(success.get()).isEqualTo(1000);
        UserAccount userAccount = accountService.getUserAccount(userId);
        assertThat(userAccount.getBalance()).isEqualTo(balance + chargeAmount * success.get());
    }


    @DisplayName("레디스 분산락을 사용해 포인트 충전을 1000번 할 경우 모두 성공한다.")
    @Test
    void attemptChargeWithRedisRLock1000Times() throws InterruptedException {
        Long userId = 1L;
        long balance = 100;
        long chargeAmount = 1000;
        int chargeCount = 1000;

        UserAccount account = userAccountJpaRepository.save(UserAccount.builder()
                                                                       .userId(userId)
                                                                       .balance(balance)
                                                                       .build());
        ExecutorService executorService = Executors.newFixedThreadPool(chargeCount);
        CountDownLatch latch = new CountDownLatch(chargeCount);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        for (int i = 0; i < chargeCount; i++) {
            executorService.submit(() -> {
                String lockKey = "user:account:" + userId;
                Boolean lock = false;
                try {
                    lock = rLockRepository.tryLock(lockKey, 5000L, 3000L, TimeUnit.MILLISECONDS);
                    if (lock) {
                        accountService.chargeBalance(new UserAccountCommand.Action(userId, chargeAmount));
                        success.incrementAndGet();
                    }
                } catch (Exception e) {
                    log.warn("## charge error:[{}]", e.getClass().getSimpleName(), e);
                    failed.incrementAndGet();
                } finally {
                    if (lock) {
                        rLockRepository.unlock(lockKey);
                    }
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        assertThat(success.get()).isEqualTo(1000);
        UserAccount userAccount = accountService.getUserAccount(userId);
        assertThat(userAccount.getBalance()).isEqualTo(balance + chargeAmount * success.get());
    }



}

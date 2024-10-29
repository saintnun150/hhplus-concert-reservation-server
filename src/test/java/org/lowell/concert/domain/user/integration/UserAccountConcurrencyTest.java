package org.lowell.concert.domain.user.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.application.support.DatabaseCleanUp;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.user.dto.UserAccountCommand;
import org.lowell.concert.domain.user.model.UserAccount;
import org.lowell.concert.domain.user.service.UserAccountService;
import org.lowell.concert.infra.db.user.repository.UserAccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private DatabaseCleanUp databaseCleanUp;

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



//    @DisplayName("포인트 충전 시 충전한 만큼 모두 증가한다")
//    @Test
//    void chargeBalanceConcurrency() throws InterruptedException {
//        // given
//        Long userId = 1L;
//        long balance = 100;
//        long chargeAmount = 1000;
//        int chargeCount = 10;
//
//        UserAccount account = userAccountJpaRepository.save(UserAccount.builder()
//                                                                       .userId(userId)
//                                                                       .balance(balance)
//                                                                       .build());
//        // when
//        ExecutorService executorService = Executors.newFixedThreadPool(chargeCount);
//        CountDownLatch latch = new CountDownLatch(chargeCount);
//
//        AtomicInteger success = new AtomicInteger(0);
//        AtomicInteger failed = new AtomicInteger(0);
//
//        for (int i = 0; i < chargeCount; i++) {
//            executorService.submit(() -> {
//                try {
//                    accountService.chargeBalance(new UserAccountCommand.Action(userId, chargeAmount));
//                    success.incrementAndGet();
//                } catch (Exception e) {
//                    failed.incrementAndGet();
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//        executorService.shutdown();
//
//        // then
//        assertThat(success.get()).isEqualTo(chargeCount);
//        UserAccount userAccount = accountService.getUserAccount(userId);
//        assertThat(userAccount.getBalance()).isEqualTo(balance + chargeAmount * chargeCount);
//    }


}

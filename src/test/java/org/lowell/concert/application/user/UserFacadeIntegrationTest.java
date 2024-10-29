package org.lowell.concert.application.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.application.support.DatabaseCleanUp;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.user.exception.UserAccountError;
import org.lowell.concert.domain.user.model.User;
import org.lowell.concert.domain.user.model.UserAccount;
import org.lowell.concert.infra.db.user.repository.UserAccountJpaRepository;
import org.lowell.concert.infra.db.user.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Slf4j
@SpringBootTest
public class UserFacadeIntegrationTest {

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

    @DisplayName("계좌 정보 조회")
    @Test
    void getAccountInfo() {
        // given
        User test1 = userJpaRepository.save(User.builder()
                                                .username("test1")
                                                .build());
        userAccountJpaRepository.save(UserAccount.builder()
                                                 .userId(test1.getUserId())
                                                 .balance(1000L)
                                                 .build());

        UserInfo.AccountInfo accountInfo = userFacade.getAccountInfo(test1.getUserId());

        assertThat(accountInfo).isNotNull();
        assertThat(accountInfo.getAccountId()).isNotNull();
        assertThat(accountInfo.getUserId()).isEqualTo(test1.getUserId());
    }

    @DisplayName("포인트 충전시 금액이 0이하면 예외가 발생한다")
    @Test
    void exceptionChargeBalance() {
        // given
        User test1 = userJpaRepository.save(User.builder()
                                                .username("test1")
                                                .build());
        userAccountJpaRepository.save(UserAccount.builder()
                                                 .userId(test1.getUserId())
                                                 .balance(1000L)
                                                 .build());

        assertThatThrownBy(() -> userFacade.chargeBalance(test1.getUserId(), 0L))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(UserAccountError.INVALID_AMOUNT);
                });

    }

    @DisplayName("포인트 충전을 동시에 여러번 시도되었을 때 한 번의 결과만 반영된다.")
    @Test
    void balanceIs1000WhenMultipleChargingToSameAccount() throws InterruptedException {
        // given
        User test1 = userJpaRepository.save(User.builder()
                                                .username("test1")
                                                .build());
        userAccountJpaRepository.save(UserAccount.builder()
                                                 .userId(test1.getUserId())
                                                 .balance(1000L)
                                                 .build());
        int threadCount = 3;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userFacade.chargeBalance(test1.getUserId(), 1000L);
                    success.incrementAndGet();
                } catch (Exception e) {
                    log.error("## error : {}", e.getLocalizedMessage());
                    log.error("## error classname : {}", e.getClass().getSimpleName());
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
                                    assertThat(userAccount.getBalance()).isEqualTo(2000L);
                                });
        assertThat(success.get()).isEqualTo(1);
    }

}

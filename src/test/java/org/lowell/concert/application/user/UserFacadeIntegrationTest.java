package org.lowell.concert.application.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.user.exception.UserAccountError;
import org.lowell.concert.domain.user.model.User;
import org.lowell.concert.domain.user.model.UserAccount;
import org.lowell.concert.infra.db.user.repository.UserAccountJpaRepository;
import org.lowell.concert.infra.db.user.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class UserFacadeIntegrationTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    UserAccountJpaRepository userAccountJpaRepository;

    @Autowired
    private UserFacade userFacade;

    @AfterEach
    void tearDown() {
        userJpaRepository.deleteAll();
        userAccountJpaRepository.deleteAll();
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
}

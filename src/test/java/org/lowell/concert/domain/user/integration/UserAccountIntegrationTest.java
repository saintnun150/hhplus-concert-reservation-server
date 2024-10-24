package org.lowell.concert.domain.user.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.user.model.UserAccount;
import org.lowell.concert.domain.user.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.lowell.concert.domain.user.exception.UserAccountErrorCode.EXCEED_BALANCE;
import static org.lowell.concert.domain.user.exception.UserAccountErrorCode.INVALID_AMOUNT;

@SpringBootTest
public class UserAccountIntegrationTest {

    @Autowired
    private UserAccountService userAccountService;

    @AfterEach
    void tearDown() {
        userAccountService.deleteAll();
    }

    @DisplayName("포인트 충전할 때 금액이 0이하면 예외가 발생한다")
    @Test
    void throwExceptionWhenPointAmountIsNegative() {
        userAccountService.createUserAccount(1L);
        UserAccount userAccount = userAccountService.getUserAccount(1L);

        assertThatThrownBy(() -> userAccount.chargeBalance(0L))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(INVALID_AMOUNT);
                });

    }

    @DisplayName("포인트 사용 시 사용금액이 0이하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenPointUsageIsNegative() {
        userAccountService.createUserAccount(1L);
        UserAccount userAccount = userAccountService.getUserAccount(1L);

        assertThatThrownBy(() -> userAccount.useBalance(0L))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(INVALID_AMOUNT);
                });
    }

    @DisplayName("포인트 사용 시 사용금액이 초과되면 예외가 발생한다.")
    @Test
    void throwExceptionWhenPointUsageExceedsBalance() {
        userAccountService.createUserAccount(1L);
        UserAccount userAccount = userAccountService.getUserAccount(1L);

        assertThatThrownBy(() -> userAccount.useBalance(1000L))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(EXCEED_BALANCE);
                });
    }

}

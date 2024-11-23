package org.lowell.apps.user.domain.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.support.DatabaseCleanUp;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.user.domain.model.UserAccount;
import org.lowell.apps.user.domain.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.lowell.apps.user.domain.exception.UserAccountError.EXCEED_BALANCE;
import static org.lowell.apps.user.domain.exception.UserAccountError.INVALID_AMOUNT;

//@ActiveProfiles("test")
@SpringBootTest
public class UserAccountIntegrationTest {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.execute();
    }

    @DisplayName("포인트 충전할 때 금액이 0이하면 예외가 발생한다")
    @Test
    void throwExceptionWhenPointAmountIsNegative() {
        userAccountService.createUserAccount(1L);
        UserAccount userAccount = userAccountService.getUserAccount(1L);

        assertThatThrownBy(() -> userAccount.chargeBalance(0L))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(INVALID_AMOUNT);
                });

    }

    @DisplayName("포인트 사용 시 사용금액이 0이하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenPointUsageIsNegative() {
        userAccountService.createUserAccount(1L);
        UserAccount userAccount = userAccountService.getUserAccount(1L);

        assertThatThrownBy(() -> userAccount.useBalance(0L))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(INVALID_AMOUNT);
                });
    }

    @DisplayName("포인트 사용 시 사용금액이 초과되면 예외가 발생한다.")
    @Test
    void throwExceptionWhenPointUsageExceedsBalance() {
        userAccountService.createUserAccount(1L);
        UserAccount userAccount = userAccountService.getUserAccount(1L);

        assertThatThrownBy(() -> userAccount.useBalance(1000L))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(EXCEED_BALANCE);
                });
    }

}

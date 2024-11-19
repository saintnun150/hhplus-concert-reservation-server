package org.lowell.apps.domain.user.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.user.domain.exception.UserAccountError;
import org.lowell.apps.user.domain.model.UserAccount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserAccountTest {

    @DisplayName("포인트 충전할 때 금액이 0이하면 예외가 발생한다")
    @Test
    void throwExceptionWhenPointAmountIsNegative() {
        Long accountId = 1L;
        Long amount = 0L;
        UserAccount account = UserAccount.builder()
                                         .userId(1L)
                                         .balance(500L)
                                         .build();


        assertThatThrownBy(() -> account.chargeBalance(amount))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(UserAccountError.INVALID_AMOUNT);
                });

    }

    @DisplayName("포인트 사용 시 사용금액이 0이하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenPointUsageIsNegative() {
        Long accountId = 1L;
        Long amount = 0L;
        UserAccount account = UserAccount.builder()
                                         .userId(1L)
                                         .balance(500L)
                                         .build();

        assertThatThrownBy(() -> account.useBalance(amount))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(UserAccountError.INVALID_AMOUNT);
                });
    }

    @DisplayName("포인트 사용 시 사용금액이 초과되면 예외가 발생한다.")
    @Test
    void throwExceptionWhenPointUsageExceedsBalance() {
        Long accountId = 1L;
        Long amount = 1000L;
        UserAccount account = UserAccount.builder()
                                         .userId(1L)
                                         .balance(500L)
                                         .build();

        assertThatThrownBy(() -> account.useBalance(amount))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(UserAccountError.EXCEED_BALANCE);
                });

    }
}

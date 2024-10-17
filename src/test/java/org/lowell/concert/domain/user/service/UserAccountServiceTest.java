package org.lowell.concert.domain.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.user.dto.UserAccountCommand;
import org.lowell.concert.domain.user.exception.UserAccountErrorCode;
import org.lowell.concert.domain.user.exception.UserException;
import org.lowell.concert.domain.user.model.TransactionType;
import org.lowell.concert.domain.user.model.UserAccountInfo;
import org.lowell.concert.domain.user.repository.UserAccountRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountService userAccountService;

    @DisplayName("없는 계좌 번호로 조회하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenAccountNumberNotFound() {
        Long accountId = 0L;
        when(userAccountRepository.getUserAccount(accountId)).thenReturn(null);
        UserAccountCommand.Action command = new UserAccountCommand.Action(accountId, 1000, TransactionType.CHARGE);
        assertThatThrownBy(() -> userAccountService.changeAccountBalance(command))
                .isInstanceOfSatisfying(UserException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(UserAccountErrorCode.NOT_FOUND_ACCOUNT);
                });

    }

    @DisplayName("포인트 충전 또는 사용할 때 금액이 0이하면 예외가 발생한다")
    @Test
    void throwExceptionWhenPointAmountIsNegative() {
        Long accountId = 1L;
        Long amount = 0L;
        UserAccountCommand.Action command = new UserAccountCommand.Action(accountId, amount, TransactionType.CHARGE);

        assertThatThrownBy(() -> userAccountService.changeAccountBalance(command))
                .isInstanceOfSatisfying(UserException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(UserAccountErrorCode.INVALID_AMOUNT);
                });
        
    }

    @DisplayName("포인트 사용 시 잔액을 초과하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenPointUsageExceedsBalance() {
        Long accountId = 1L;
        Long amount = 1000L;
        UserAccountInfo account = UserAccountInfo.builder()
                                                 .userAccountId(accountId)
                                                 .userId(1L)
                                                 .balance(500L)
                                                 .createdAt(LocalDateTime.now())
                                                 .updatedAt(null)
                                                 .build();
        when(userAccountRepository.getUserAccount(accountId)).thenReturn(account);

        UserAccountCommand.Action command = new UserAccountCommand.Action(accountId, amount, TransactionType.USE);

        assertThatThrownBy(() -> userAccountService.changeAccountBalance(command))
                .isInstanceOfSatisfying(UserException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(UserAccountErrorCode.EXCEED_BALANCE);
                });
    }
}
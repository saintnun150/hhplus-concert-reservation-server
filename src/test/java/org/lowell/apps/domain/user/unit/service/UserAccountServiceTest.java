package org.lowell.apps.domain.user.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.user.domain.exception.UserAccountError;
import org.lowell.apps.user.domain.repository.UserAccountRepository;
import org.lowell.apps.user.domain.service.UserAccountService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
        Long userId = 0L;
        when(userAccountRepository.getUserAccountByUserId(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userAccountService.getUserAccount(userId))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(UserAccountError.NOT_FOUND_ACCOUNT);
                });

    }
}
package org.lowell.concert.domain.user.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.application.user.UserFacade;
import org.lowell.concert.application.user.UserInfo;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.user.exception.UserAccountErrorCode;
import org.lowell.concert.domain.user.service.UserAccountService;
import org.lowell.concert.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class UserFacadeIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserFacade userFacade;

    @BeforeEach
    void setUp() {
        userService.deleteAllUser();
        userAccountService.deleteAll();
    }

    @DisplayName("계좌 정보 조회")
    @Test
    void getAccountInfo() {
        // given
        Long userId = 1L;
        userService.createUser(userId, "test1");
        userAccountService.createUserAccount(userId);

        UserInfo.AccountInfo accountInfo = userFacade.getAccountInfo(userId);

        assertThat(accountInfo).isNotNull();
        assertThat(accountInfo.getAccountId()).isNotNull();
        assertThat(accountInfo.getUserId()).isEqualTo(userId);
    }

    @DisplayName("포인트 충전시 금액이 0이하면 예외가 발생한다")
    @Test
    void exceptionChargeBalance() {
        // given
        Long userId = 1L;
        userService.createUser(userId, "test1");
        userAccountService.createUserAccount(userId);

        assertThatThrownBy(() -> userFacade.chargeBalance(userId, 0L))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(UserAccountErrorCode.INVALID_AMOUNT);
                });

    }
}

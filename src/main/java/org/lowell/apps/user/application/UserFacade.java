package org.lowell.apps.user.application;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.user.domain.dto.UserAccountCommand;
import org.lowell.apps.user.domain.model.User;
import org.lowell.apps.user.domain.model.UserAccount;
import org.lowell.apps.user.domain.service.UserAccountService;
import org.lowell.apps.user.domain.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final UserAccountService userAccountService;

    public UserInfo.AccountInfo getAccountInfo(Long userId) {
        User user = userService.getUser(userId);
        UserAccount userAccount = userAccountService.getUserAccount(userId);
        return new UserInfo.AccountInfo(userAccount.getAccountId(),
                                        user.getUserId(),
                                        user.getUsername(),
                                        userAccount.getBalance(),
                                        userAccount.getCreatedAt(),
                                        userAccount.getUpdatedAt());
    }

    @Transactional
    public UserInfo.AccountInfo chargeBalance(Long userId, Long balance) {
        User user = userService.getUser(userId);
        UserAccount userAccount = userAccountService.chargeBalance(new UserAccountCommand.Action(userId, balance));
        return new UserInfo.AccountInfo(userAccount.getAccountId(),
                                        user.getUserId(),
                                        user.getUsername(),
                                        userAccount.getBalance(),
                                        userAccount.getCreatedAt(),
                                        userAccount.getUpdatedAt());
    }

    @Transactional
    public UserInfo.AccountInfo chargeBalanceWithLock(Long userId, Long balance) {
        User user = userService.getUser(userId);
        UserAccount userAccount = userAccountService.chargeBalanceWithLock(new UserAccountCommand.Action(userId, balance));
        return new UserInfo.AccountInfo(userAccount.getAccountId(),
                                        user.getUserId(),
                                        user.getUsername(),
                                        userAccount.getBalance(),
                                        userAccount.getCreatedAt(),
                                        userAccount.getUpdatedAt());
    }
}

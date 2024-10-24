package org.lowell.concert.application.user;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.user.dto.UserAccountCommand;
import org.lowell.concert.domain.user.model.User;
import org.lowell.concert.domain.user.model.UserAccount;
import org.lowell.concert.domain.user.service.UserAccountService;
import org.lowell.concert.domain.user.service.UserService;
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
}

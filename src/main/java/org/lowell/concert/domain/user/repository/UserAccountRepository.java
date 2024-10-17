package org.lowell.concert.domain.user.repository;

import org.lowell.concert.domain.user.dto.UserAccountCommand;
import org.lowell.concert.domain.user.model.UserAccountInfo;

public interface UserAccountRepository {
    UserAccountInfo getUserAccount(Long id);
    UserAccountInfo getUserAccountByUserId(Long userId);
    void updateUserAccount(UserAccountCommand.Update command);
}

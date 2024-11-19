package org.lowell.apps.user.domain.repository;

import org.lowell.apps.user.domain.model.UserAccount;

import java.util.Optional;

public interface UserAccountRepository {
    void createUserAccount(Long userId);
    void deleteAll();
    Optional<UserAccount> getUserAccountByUserId(Long userId);
    Optional<UserAccount> getUserAccountByUserIdWithLock(Long userId);
}

package org.lowell.concert.domain.user.repository;

import org.lowell.concert.domain.user.model.UserAccount;

import java.util.Optional;

public interface UserAccountRepository {
    void createUserAccount(Long userId);
    void deleteAll();
    Optional<UserAccount> getUserAccountByUserId(Long userId);
    Optional<UserAccount> getUserAccountByUserIdWithLock(Long userId);
}

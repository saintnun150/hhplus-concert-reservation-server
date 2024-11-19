package org.lowell.apps.user.infra.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.user.domain.model.UserAccount;
import org.lowell.apps.user.domain.repository.UserAccountRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAccountRepositoryImpl implements UserAccountRepository {
    private final UserAccountJpaRepository jpaRepository;

    @Transactional
    @Override
    public void createUserAccount(Long userId) {
        UserAccount account = UserAccount.createAccount(userId);
        jpaRepository.save(account);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public Optional<UserAccount> getUserAccountByUserId(Long userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<UserAccount> getUserAccountByUserIdWithLock(Long userId) {
        return jpaRepository.findByUserIdWithLock(userId);
    }
}

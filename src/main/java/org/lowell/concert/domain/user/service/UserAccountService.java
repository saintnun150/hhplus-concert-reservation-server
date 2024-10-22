package org.lowell.concert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.user.dto.UserAccountCommand;
import org.lowell.concert.domain.user.exception.UserAccountErrorCode;
import org.lowell.concert.domain.user.model.UserAccount;
import org.lowell.concert.domain.user.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;

    public void createUserAccount(Long userId) {
        userAccountRepository.createUserAccount(userId);
    }

    public void deleteAll() {
        userAccountRepository.deleteAll();
    }

    public UserAccount getUserAccount(Long userId) {
        return userAccountRepository.getUserAccountByUserId(userId)
                                    .orElseThrow(() -> new DomainException(UserAccountErrorCode.NOT_FOUND_ACCOUNT));
    }

    public UserAccount getUserAccountWithLock(Long userId) {
        return userAccountRepository.getUserAccountByUserIdWithLock(userId)
                                    .orElseThrow(() -> new DomainException(UserAccountErrorCode.NOT_FOUND_ACCOUNT));
    }

    @Transactional
    public UserAccount chargeBalance(UserAccountCommand.Action action) {
        UserAccount userAccount = getUserAccountWithLock(action.userId());
        userAccount.chargeBalance(action.amount());
        return userAccount;
    }

    @Transactional
    public UserAccount useBalance(UserAccountCommand.Action action) {
        UserAccount userAccount = getUserAccountWithLock(action.userId());
        userAccount.useBalance(action.amount());
        return userAccount;
    }

}

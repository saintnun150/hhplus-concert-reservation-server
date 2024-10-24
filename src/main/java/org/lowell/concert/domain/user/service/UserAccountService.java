package org.lowell.concert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.user.dto.UserAccountCommand;
import org.lowell.concert.domain.user.exception.UserAccountErrorCode;
import org.lowell.concert.domain.user.exception.UserException;
import org.lowell.concert.domain.user.model.TransactionType;
import org.lowell.concert.domain.user.model.UserAccountInfo;
import org.lowell.concert.domain.user.repository.UserAccountHistoryRepository;
import org.lowell.concert.domain.user.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final UserAccountHistoryRepository historyRepository;

    public UserAccountInfo getUserAccount(Long accountId) {
        return userAccountRepository.getUserAccount(accountId);
    }
    public UserAccountInfo getUserAccountByUserId(Long userId) {
        return userAccountRepository.getUserAccountByUserId(userId);
    }
    public void changeAccountBalance(UserAccountCommand.Action command) {
        if (command.amount() <= 0) {
            throw new UserException(UserAccountErrorCode.INVALID_AMOUNT);
        }
        UserAccountInfo userAccount = getUserAccount(command.accountId());
        if (userAccount == null) {
            throw new UserException(UserAccountErrorCode.NOT_FOUND_ACCOUNT);
        }
        if (command.transactionType() == TransactionType.USE) {
            if (userAccount.getBalance() < command.amount()) {
                throw new UserException(UserAccountErrorCode.EXCEED_BALANCE);
            }
        }
        userAccount.changeBalance(command.transactionType(), command.amount());
        UserAccountCommand.Update update = new UserAccountCommand.Update(command.accountId(),
                                                                         userAccount.getBalance());
        userAccountRepository.updateUserAccount(update);
        historyRepository.insert(command);
    }
}

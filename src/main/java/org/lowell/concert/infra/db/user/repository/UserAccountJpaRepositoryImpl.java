package org.lowell.concert.infra.db.user.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.user.UserAccountMapper;
import org.lowell.concert.domain.user.dto.UserAccountCommand;
import org.lowell.concert.domain.user.model.UserAccountInfo;
import org.lowell.concert.domain.user.repository.UserAccountRepository;
import org.lowell.concert.infra.db.user.entity.UserAccountEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserAccountJpaRepositoryImpl implements UserAccountRepository {
    private final UserAccountJpaRepository jpaRepository;
    private final UserAccountMapper mapper;

    @Override
    public UserAccountInfo getUserAccount(Long id) {
        UserAccountEntity entity = jpaRepository.findById(id)
                                                .orElse(null);
        return mapper.toPojo(entity);
    }

    @Override
    public UserAccountInfo getUserAccountByUserId(Long userId) {
        UserAccountEntity entity = jpaRepository.findByUserId(userId);
        return mapper.toPojo(entity);
    }

    @Transactional
    @Override
    public void updateUserAccount(UserAccountCommand.Update command) {
        jpaRepository.findById(command.accountId())
                             .ifPresent(account -> {
                                 account.changeBalance(command.balance());
                             });
    }
}

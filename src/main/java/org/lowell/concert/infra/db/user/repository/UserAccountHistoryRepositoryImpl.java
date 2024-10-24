package org.lowell.concert.infra.db.user.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.user.dto.UserAccountCommand;
import org.lowell.concert.domain.user.repository.UserAccountHistoryRepository;
import org.lowell.concert.infra.db.user.entity.UserAccountHistoryEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class UserAccountHistoryRepositoryImpl implements UserAccountHistoryRepository {
    private final UserAccountHistoryJpaRepository jpaRepository;


    @Transactional
    @Override
    public void insert(UserAccountCommand.Action command) {
        UserAccountHistoryEntity entity = UserAccountHistoryEntity.builder()
                                                                  .userAccountId(command.accountId())
                                                                  .amount(command.amount())
                                                                  .transactionType(command.transactionType())
                                                                  .createdAt(LocalDateTime.now())
                                                                  .build();
        jpaRepository.save(entity);
    }
}
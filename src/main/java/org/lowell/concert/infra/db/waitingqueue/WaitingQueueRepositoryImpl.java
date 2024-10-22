package org.lowell.concert.infra.db.waitingqueue;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {
    private final WaitingQueueTokenJpaRepository jpaRepository;

    @Override
    public WaitingQueue createWaitingQueue(WaitingQueueCommand.Create command) {
        WaitingQueue entity = WaitingQueue.builder()
                                          .token(command.token())
                                          .tokenStatus(command.status())
                                          .createdAt(LocalDateTime.now())
                                          .expiresAt(command.expiresAt())
                                          .build();

        jpaRepository.save(entity);
        return entity;
    }

    @Override
    public Optional<WaitingQueue> getWaitingQueue(WaitingQueueQuery.GetQueue query) {
       return jpaRepository.findByToken(query.token());
    }

    @Override
    public Long getWaitingOrder(WaitingQueueQuery.Order query) {
        return jpaRepository.countByTokenIdLessThanEqualAndTokenStatus(query.tokenId(), query.tokenStatus());
    }

    @Override
    public Long getActivateQueueCount(TokenStatus tokenStatus) {
        return jpaRepository.countByTokenStatus(tokenStatus);
    }

    @Override
    public List<WaitingQueue> getWaitingQueues(WaitingQueueQuery.GetQueues query) {
        return jpaRepository.findWaitingQueuesByStatus(query.tokenStatus(),
                                                PageRequest.of(0, (int) query.size()));
    }

    @Override
    public void update(WaitingQueueCommand.Update command) {
        jpaRepository.updateByTokenIdInAndTokenStatus(command.tokenId(),
                                                      command.status(),
                                                      command.updatedAt());
    }

    @Override
    public void updateAll(WaitingQueueCommand.UpdateBatch command) {
        jpaRepository.updateAllByTokenIdInAndTokenStatusAndExpiresAt(command.tokenIds(),
                                                                     command.status(),
                                                                     command.updatedAt(),
                                                                     command.expiresAt());
    }

    @Transactional
    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

}

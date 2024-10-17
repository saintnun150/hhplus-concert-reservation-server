package org.lowell.concert.infra.db.waitingqueue;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.waitingqueue.WaitingQueueTokenMapper;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueTokenCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueTokenQuery;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueTokenRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaitingQueueTokenRepositoryImpl implements WaitingQueueTokenRepository {
    private final WaitingQueueTokenJpaRepository jpaRepository;
    private final WaitingQueueTokenMapper mapper;

    @Override
    @Transactional
    public WaitingQueueTokenInfo createQueueToken(WaitingQueueTokenCommand.Create command) {
        WaitingQueueTokenEntity entity = WaitingQueueTokenEntity.builder()
                                                                .token(command.token())
                                                                .tokenStatus(command.status())
                                                                .createdAt(LocalDateTime.now())
                                                                .build();
        WaitingQueueTokenEntity savedToken = jpaRepository.save(entity);
        return mapper.toPojo(savedToken);
    }

    @Override
    public WaitingQueueTokenInfo getQueueToken(Long tokenId) {
        WaitingQueueTokenEntity tokenEntity = jpaRepository.findById(tokenId)
                                                           .orElse(null);
        return mapper.toPojo(tokenEntity);
    }

    @Override
    public WaitingQueueTokenInfo getQueueToken(String token) {
        WaitingQueueTokenEntity tokenEntity = jpaRepository.findByToken(token)
                                                           .orElse(null);
        return mapper.toPojo(tokenEntity);
    }

    @Override
    public int getTokenCountByStatus(TokenStatus status) {
        Long count = jpaRepository.countByTokenStatus(status);
        if (count == null) {
            return 0;
        }
        return count.intValue();
    }

    @Override
    public int getTokenOrder(WaitingQueueTokenQuery.Order query) {
        Long count = jpaRepository.countByTokenIdLessThanEqualAndTokenStatus(query.tokenId(), query.tokenStatus());
        if (count == null) {
            return 0;
        }
        return count.intValue();
    }

    @Override
    public List<WaitingQueueTokenInfo> getTokensByTokenStatusWithLimit(WaitingQueueTokenQuery.Update query) {
        List<WaitingQueueTokenEntity> tokenEntities = jpaRepository.findTokensByTokenStatusWithLimit(query.tokenStatus(),
                                                                                              PageRequest.of(0, 5));
        return mapper.toPojoList(tokenEntities);
    }

    @Override
    public void batchUpdateTokenStatus(WaitingQueueTokenCommand.Update command) {
        jpaRepository.updateAllByTokenIdInAndTokenStatusAndExpiresAt(command.tokenIds(),
                                                                     command.status(),
                                                                     command.updatedAt(),
                                                                     command.expiresAt());
    }


}

package org.lowell.apps.waitingqueue.infra.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.apps.concert.domain.ConcertPolicy;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueCommand;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueQuery;
import org.lowell.apps.waitingqueue.domain.model.TokenStatus;
import org.lowell.apps.waitingqueue.domain.model.WaitingQueueTokenInfo;
import org.lowell.apps.waitingqueue.domain.repository.WaitingQueueRepository;
import org.lowell.apps.waitingqueue.infra.redis.RedisRepository;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {
    private final RedisRepository redisRepository;
    private static final String WAITING_QUEUE_KEY_PREFIX = "waitingQueue";
    private static final String ACTIVATE_QUEUE_KEY_PREFIX = "activatedToken";

    @Override
    public WaitingQueueTokenInfo createQueueToken(WaitingQueueCommand.Create command) {
        redisRepository.addZSetIfAbsent(WAITING_QUEUE_KEY_PREFIX,
                                            command.token(),
                                            System.currentTimeMillis());
        return new WaitingQueueTokenInfo(command.token(),
                                         TokenStatus.WAITING,
                                         null);
    }

    @Override
    public Optional<WaitingQueueTokenInfo> findWaitingQueueToken(WaitingQueueQuery.GetToken query) {
        return redisRepository.getZScore(WAITING_QUEUE_KEY_PREFIX, query.token()) != null
                ? Optional.of(new WaitingQueueTokenInfo(query.token(), TokenStatus.WAITING, null))
                : Optional.empty();
    }

    @Override
    public Optional<WaitingQueueTokenInfo> findActivateQueueToken(WaitingQueueQuery.GetToken query) {
        return Optional.ofNullable(redisRepository.getValue(query.token()))
                       .map(value -> LocalDateTime.parse((String) value, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                       .map(expiresAt -> new WaitingQueueTokenInfo(query.token(), TokenStatus.ACTIVATE, expiresAt));
    }

    @Override
    public Long findWaitingTokenOrder(WaitingQueueQuery.GetToken query) {
        return redisRepository.getZSetRank(WAITING_QUEUE_KEY_PREFIX, query.token());
    }

    @Override
    public void activateWaitingToken(WaitingQueueQuery.GetQueues query) {
        Set<ZSetOperations.TypedTuple<Object>> tokens = redisRepository.zPopMin(WAITING_QUEUE_KEY_PREFIX, query.size());
        for (ZSetOperations.TypedTuple<Object> tuple : tokens) {
            Object token = tuple.getValue();
            String key = ACTIVATE_QUEUE_KEY_PREFIX + token.toString();
            redisRepository.addIfAbsent(key, query.expiresAt().toString(), ConcertPolicy.EXPIRED_QUEUE_MINUTES, TimeUnit.MINUTES);
        }
    }

    @Override
    public void expireQueueToken(WaitingQueueCommand.ExpireToken command) {
        redisRepository.deleteKey(command.token());
    }

    @Override
    public Long findTokenCount(TokenStatus tokenStatus) {
        if (tokenStatus == TokenStatus.WAITING) {
            return redisRepository.getZSetSize(WAITING_QUEUE_KEY_PREFIX);
        }
        return null;
    }
}

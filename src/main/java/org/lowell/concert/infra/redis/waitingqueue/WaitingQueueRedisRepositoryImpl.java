package org.lowell.concert.infra.redis.waitingqueue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueToken;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueRepository;
import org.lowell.concert.infra.redis.support.RedisRepository;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WaitingQueueRedisRepositoryImpl implements WaitingQueueRepository {
    private final RedisRepository redisRepository;
    private static final String WAITING_QUEUE_KEY_PREFIX = "waitingQueue";
    private static final String ACTIVATE_QUEUE_KEY_PREFIX = "activatedToken";

    @Override
    public WaitingQueueTokenInfo createQueueToken(WaitingQueueCommand.CreateToken command) {
        if (command.status() == TokenStatus.ACTIVATE) {
            redisRepository.addIfAbsent(command.token(),
                                        command.expiresAt().toString(),
                                        ConcertPolicy.EXPIRED_QUEUE_MINUTES,
                                        TimeUnit.MINUTES);
        } else {
            redisRepository.addZSetIfAbsent(WAITING_QUEUE_KEY_PREFIX,
                                            command.token(),
                                            System.currentTimeMillis());
        }
        return new WaitingQueueTokenInfo(command.token(),
                                         command.status(),
                                         command.expiresAt());
    }

    @Override
    public Optional<WaitingQueueTokenInfo> findQueueToken(WaitingQueueQuery.GetToken query) {
        Long waitRank = redisRepository.getZSetRank(WAITING_QUEUE_KEY_PREFIX, query.token());
        if (waitRank != null) {
            return Optional.of(new WaitingQueueTokenInfo(query.token(),
                                                         TokenStatus.WAITING,
                                                         null));
        }

        return Optional.ofNullable(redisRepository.getValue(query.token()))
                       .map(value -> LocalDateTime.parse((String) value, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                       .map(expiresAt -> new WaitingQueueTokenInfo(query.token(),
                                                                   TokenStatus.ACTIVATE,
                                                                   expiresAt));
    }

    @Override
    public Long findWaitingTokenOrder(WaitingQueueQuery.GetOrder query) {
        return redisRepository.getZSetRank(WAITING_QUEUE_KEY_PREFIX, query.token());
    }

    @Override
    public void activateWaitingToken(WaitingQueueQuery.GetQueues query) {
        Set<ZSetOperations.TypedTuple<Object>> tokens = redisRepository.zPopMin(WAITING_QUEUE_KEY_PREFIX, query.size());
        for (ZSetOperations.TypedTuple<Object> tuple : tokens) {
            Object token = tuple.getValue();
            String key = ACTIVATE_QUEUE_KEY_PREFIX + token.toString();
            redisRepository.addIfAbsent(key,
                                        query.expiresAt().toString(),
                                        ConcertPolicy.EXPIRED_QUEUE_MINUTES,
                                        TimeUnit.MINUTES);
        }
    }


    @Override
    public boolean existsActivateToken(String token) {
        return redisRepository.existsKey(token);
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

    @Override
    public List<WaitingQueueToken> findQueuesByStatusAndSize(WaitingQueueQuery.GetQueues query) {
        return List.of();
    }
}

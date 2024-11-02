package org.lowell.concert.infra.redis.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.domain.common.support.lock.LockRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRLockRepository implements LockRepository {
    private final RedisRepository redisRepository;

    @Override
    public Boolean tryLock(String lockKey, String value, Long leaseTime, TimeUnit timeUnit) {
        return false;
    }

    @Override
    public Boolean tryLock(String lockKey, String value, Long waitTime, Long leaseTime, TimeUnit timeUnit) {
        try {
            return redisRepository.getLock(lockKey)
                                  .tryLock(leaseTime, waitTime, timeUnit);
        } catch (InterruptedException e) {
            log.error("## Failed to RLock acquirement", e);
            return false;
        }
    }

    @Override
    public Boolean unlock(String lockKey) {
        redisRepository.unlock(lockKey);
        return true;
    }
}

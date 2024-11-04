package org.lowell.concert.infra.redis.support;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.support.lock.LockManager;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisSimpleLockManager implements LockManager {
    private final RedisRepository redisRepository;

    @Override
    public Boolean tryLock(String lockKey, Long waitTime, Long leaseTime, TimeUnit timeUnit) {
        long lockValue = System.currentTimeMillis() + timeUnit.toMillis(waitTime);
        return redisRepository.setIfAbsent(lockKey, Long.toString(lockValue), leaseTime, timeUnit);
    }

    @Override
    public void unlock(String lockKey) {
        redisRepository.delete(lockKey);
    }
}

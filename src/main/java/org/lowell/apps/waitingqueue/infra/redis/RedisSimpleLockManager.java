package org.lowell.apps.waitingqueue.infra.redis;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.common.lock.LockManager;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisSimpleLockManager implements LockManager {
    private final RedisRepository redisRepository;

    @Override
    public Boolean tryLock(String lockKey, Long waitTime, Long leaseTime, TimeUnit timeUnit) {
        long lockValue = System.currentTimeMillis() + timeUnit.toMillis(waitTime);
        return redisRepository.addIfAbsent(lockKey, Long.toString(lockValue), leaseTime, timeUnit);
    }

    @Override
    public void unlock(String lockKey) {
        redisRepository.deleteKey(lockKey);
    }
}

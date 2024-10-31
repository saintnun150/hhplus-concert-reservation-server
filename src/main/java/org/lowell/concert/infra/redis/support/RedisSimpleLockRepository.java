package org.lowell.concert.infra.redis.support;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.support.lock.LockRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisSimpleLockRepository implements LockRepository {
    private final RedisRepository redisRepository;

    @Override
    public Boolean tryLock(String lockKey, String value, Long leaseTime, TimeUnit timeUnit) {
        return redisRepository.setIfAbsent(lockKey, value, leaseTime, timeUnit);
    }

    @Override
    public Boolean tryLock(String lockKey, String value, Long waitTime, Long leaseTime, TimeUnit timeUnit) {
        return false;
    }

    @Override
    public Boolean unlock(String lockKey) {
        return redisRepository.delete(lockKey);
    }
}

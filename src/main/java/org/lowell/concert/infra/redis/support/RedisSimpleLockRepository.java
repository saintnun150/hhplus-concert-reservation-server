package org.lowell.concert.infra.redis.support;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.support.LockRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisSimpleLockRepository implements LockRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean lock(String lockKey, Long leaseTime, TimeUnit timeUnit) {
        return redisTemplate.opsForValue()
                            .setIfAbsent(lockKey, "locked", leaseTime, timeUnit);
    }

    @Override
    public Boolean unlock(String lockKey) {
        return redisTemplate.delete(lockKey);
    }

    @Override
    public Boolean tryLock(String lockKey, Long leaseTime, TimeUnit timeUnit) {
        return null;
    }
}

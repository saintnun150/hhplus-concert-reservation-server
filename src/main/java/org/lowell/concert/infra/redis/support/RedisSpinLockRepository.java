package org.lowell.concert.infra.redis.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.domain.common.support.lock.LockRepository;
import org.lowell.concert.infra.redis.support.enums.LockType;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisSpinLockRepository implements LockRepository {
    private final RedisRepository redisRepository;

    @Override
    public Boolean tryLock(String lockKey, String value, Long leaseTime, TimeUnit timeUnit) {
        while (true) {
            if (redisRepository.setIfAbsent(lockKey, value, leaseTime, timeUnit)) {
                return true;
            }
            try {
                Thread.sleep(LockType.SPIN_LOCK.getWaitTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
    }

    @Override
    public Boolean unlock(String lockKey) {
        return redisRepository.delete(lockKey);
    }
}

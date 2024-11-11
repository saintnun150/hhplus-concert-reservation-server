package org.lowell.concert.infra.redis.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.domain.support.lock.LockManager;
import org.lowell.concert.infra.redis.support.enums.LockType;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisSpinLockManager implements LockManager {
    private final RedisRepository redisRepository;

    @Override
    public Boolean tryLock(String lockKey, Long waitTime, Long leaseTime, TimeUnit timeUnit) {
        long retryTime = System.currentTimeMillis() + timeUnit.toMillis(waitTime);
        while (true) {
            if (redisRepository.addIfAbsent(lockKey, "", leaseTime, timeUnit)) {
                return true;
            }
            try {
                Thread.sleep(LockType.SPIN_LOCK.getWaitTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
//        return false;
    }

    @Override
    public void unlock(String lockKey) {
        redisRepository.deleteKey(lockKey);
    }
}

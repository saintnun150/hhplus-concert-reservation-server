package org.lowell.apps.waitingqueue.infra.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.apps.common.lock.LockManager;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class RedisRLockManager implements LockManager {
    private final RedissonClient redissonClient;

    @Override
    public Boolean tryLock(String lockKey, Long waitTime, Long leaseTime, TimeUnit timeUnit) {
        try {
            return redissonClient.getLock(lockKey)
                                 .tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            log.error("## Failed to RLock acquirement", e);
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }
}

package org.lowell.apps.common.lock;

import java.util.concurrent.TimeUnit;

public interface LockManager {
    Boolean tryLock(final String lockKey, final Long waitTime, final Long leaseTime, TimeUnit timeUnit);
    void unlock(final String lockKey);
}

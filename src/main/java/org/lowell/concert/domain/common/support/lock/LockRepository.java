package org.lowell.concert.domain.common.support.lock;

import java.util.concurrent.TimeUnit;

public interface LockRepository {
    Boolean tryLock(final String lockKey, final String value, final Long leaseTime, TimeUnit timeUnit);
    Boolean tryLock(final String lockKey, final String value, final Long waitTime, final Long leaseTime, TimeUnit timeUnit);
    Boolean unlock(final String lockKey);
}

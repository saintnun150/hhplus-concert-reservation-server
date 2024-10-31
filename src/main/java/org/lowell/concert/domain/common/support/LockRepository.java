package org.lowell.concert.domain.common.support;

import java.util.concurrent.TimeUnit;

public interface LockRepository {
    Boolean lock(final String lockKey, final Long leaseTime, TimeUnit timeUnit);
    Boolean unlock(final String lockKey);
    Boolean tryLock(final String lockKey, final Long leaseTime, final TimeUnit timeUnit);
}

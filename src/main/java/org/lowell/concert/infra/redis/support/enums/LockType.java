package org.lowell.concert.infra.redis.support.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LockType {
    SPIN_LOCK(30, 30);

    private final int maxRetries;
    private final long waitTime;

}

package org.lowell.apps.waitingqueue.infra.redis.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LockType {
    SPIN_LOCK(30, 30);

    private final int maxRetries;
    private final long waitTime;

}

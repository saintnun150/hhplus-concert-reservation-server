package org.lowell.concert.infra.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueProvider;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueRepository;
import org.lowell.concert.infra.db.waitingqueue.WaitingQueueRepositoryImpl;
import org.lowell.concert.infra.redis.waitingqueue.WaitingQueueRedisRepositoryImpl;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingQueueProviderImpl implements WaitingQueueProvider {
    private final WaitingQueueRepositoryImpl waitingQueueRepository;
    private final WaitingQueueRedisRepositoryImpl waitingQueueRedisRepository;

    public WaitingQueueRepository getWaitingQueueRepository(String type) {
        if (type.equals("db")) {
            return waitingQueueRepository;
        }
        return waitingQueueRedisRepository;
    }
}

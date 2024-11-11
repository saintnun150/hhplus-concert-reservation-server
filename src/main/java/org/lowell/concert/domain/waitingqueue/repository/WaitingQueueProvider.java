package org.lowell.concert.domain.waitingqueue.repository;

public interface WaitingQueueProvider {
    WaitingQueueRepository getWaitingQueueRepository(String type);
}

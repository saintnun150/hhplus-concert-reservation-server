package org.lowell.concert.domain.waitingqueue.repository;

import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;

import java.util.List;
import java.util.Optional;

public interface WaitingQueueRepository {
    WaitingQueue createWaitingQueue(WaitingQueueCommand.Create command);
    Optional<WaitingQueue> getWaitingQueue(WaitingQueueQuery.GetQueue query);
    Long getWaitingOrder(WaitingQueueQuery.Order query);
    Long getActivateQueueCount(TokenStatus tokenStatus);
    List<WaitingQueue> getWaitingQueues(WaitingQueueQuery.GetQueues query);
    void update(WaitingQueueCommand.Update command);
    void updateAll(WaitingQueueCommand.UpdateBatch command);
    void deleteAll();
}

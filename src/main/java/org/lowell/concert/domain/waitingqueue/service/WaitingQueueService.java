package org.lowell.concert.domain.waitingqueue.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueError;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingQueueService {
    private static final int ACTIVATE_CAPACITY = 50;
    private final WaitingQueueRepository queueRepository;

    @Transactional
    public WaitingQueue createWaitingQueue(WaitingQueueCommand.Create command) {
        return queueRepository.createWaitingQueue(command);
    }

    public WaitingQueue getWaitingQueue(WaitingQueueQuery.GetQueue query) {
        WaitingQueue queue = queueRepository.getWaitingQueue(query)
                                            .orElseThrow(() -> DomainException.create(WaitingQueueError.NOT_FOUND_TOKEN));
        return queue;
    }

    public Long getWaitingQueueOrder(WaitingQueueQuery.GetQueue query) {
        WaitingQueue queue = queueRepository.getWaitingQueue(query)
                                            .orElseThrow(() -> DomainException.create(WaitingQueueError.NOT_FOUND_TOKEN));
        queue.validateWaitingStatus();

        Long waitingOrder = queueRepository.getWaitingOrder(new WaitingQueueQuery.Order(queue.getTokenId(), TokenStatus.WAITING));
        if (waitingOrder == null) {
            throw DomainException.create(WaitingQueueError.INVALID_WAITING_ORDER);
        }
        return waitingOrder;
    }

    public boolean createActivationQueueImmediately() {
        Long activateQueueCount = queueRepository.getActivateQueueCount(TokenStatus.ACTIVATE);
        if (activateQueueCount == null || activateQueueCount == 0) {
            return true;
        }
        return activateQueueCount < ACTIVATE_CAPACITY;
    }

    public List<WaitingQueue> getWaitingQueueByStatus(WaitingQueueQuery.GetQueues query) {
        return queueRepository.getWaitingQueues(query);
    }

    public int countReadyToActivateQueues() {
        Long activateQueueCount = queueRepository.getActivateQueueCount(TokenStatus.ACTIVATE);
        if (activateQueueCount == null || activateQueueCount == 0) {
            return ACTIVATE_CAPACITY;
        }
        return ACTIVATE_CAPACITY - activateQueueCount.intValue();
    }

    public void activateReadyWaitingQueues(WaitingQueueCommand.UpdateBatch command) {
        int count = countReadyToActivateQueues();
        if (count == 0) {
            log.info("## No capacity to activate waiting queues");
            return;
        }
        List<WaitingQueue> waitingQueues = getWaitingQueueByStatus(new WaitingQueueQuery.GetQueues(TokenStatus.WAITING,count));
        if (waitingQueues.isEmpty()) {
            log.info("## No waiting queues to activate");
            return;
        }
        List<Long> tokenIds = waitingQueues.stream()
                                           .map(WaitingQueue::getTokenId)
                                           .toList();
        command = new WaitingQueueCommand.UpdateBatch(tokenIds,
                                                      TokenStatus.ACTIVATE,
                                                      command.updatedAt(),
                                                      command.expiresAt());
        queueRepository.updateAll(command);
    }
}

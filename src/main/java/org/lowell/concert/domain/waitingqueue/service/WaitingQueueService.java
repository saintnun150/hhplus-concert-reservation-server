package org.lowell.concert.domain.waitingqueue.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueErrorCode;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueue;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

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
                                            .orElseThrow(() -> DomainException.create(WaitingQueueErrorCode.NOT_FOUND_TOKEN));
        return queue;
    }

    public Long getWaitingQueueOrder(WaitingQueueQuery.GetQueue query) {
        WaitingQueue queue = queueRepository.getWaitingQueue(query)
                                            .orElseThrow(() -> DomainException.create(WaitingQueueErrorCode.NOT_FOUND_TOKEN));
        queue.validateWaitingStatus();

        Long waitingOrder = queueRepository.getWaitingOrder(new WaitingQueueQuery.Order(queue.getTokenId(), TokenStatus.WAITING));
        if (waitingOrder == null) {
            throw DomainException.create(WaitingQueueErrorCode.INVALID_WAITING_ORDER);
        }
        return waitingOrder;
    }

    public boolean createActivationQueueImmediately() {
        Long activateQueueCount = queueRepository.getActivateQueueCount(TokenStatus.ACTIVATE);
        if (activateQueueCount == null) {
            return true;
        }
        return activateQueueCount < ACTIVATE_CAPACITY;
    }

    public List<WaitingQueue> getWaitingQueueByStatus(WaitingQueueQuery.GetQueues query) {
        return queueRepository.getWaitingQueues(query);
    }

    public void updateWaitingQueue(WaitingQueueCommand.Update command) {
        if (command.tokenId() == null) {
            throw new DomainException(WaitingQueueErrorCode.EMPTY_TOKEN_ID);
        }
        if (command.status() == null || !EnumUtils.isValidEnum(TokenStatus.class, command.status().name())) {
            throw new DomainException(WaitingQueueErrorCode.INVALID_TOKEN_STATUS_INPUT);
        }
        queueRepository.update(command);
    }

     public void updateWaitingQueues(WaitingQueueCommand.UpdateBatch command) {
         if (CollectionUtils.isEmpty(command.tokenIds())) {
             throw new DomainException(WaitingQueueErrorCode.EMPTY_TOKEN_IDS);
         }
         if (command.status() == null || !EnumUtils.isValidEnum(TokenStatus.class, command.status().name())) {
             throw new DomainException(WaitingQueueErrorCode.INVALID_TOKEN_STATUS_INPUT);
         }
         if (command.expiresAt() == null || command.expiresAt().isBefore(LocalDateTime.now())) {
             throw new DomainException(WaitingQueueErrorCode.INVALID_TOKEN_EXPIRES_DATE);
         }
         queueRepository.updateAll(command);
     }

}

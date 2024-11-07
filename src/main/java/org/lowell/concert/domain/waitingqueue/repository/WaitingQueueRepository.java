package org.lowell.concert.domain.waitingqueue.repository;

import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueToken;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;

import java.util.List;
import java.util.Optional;

public interface WaitingQueueRepository {
    WaitingQueueTokenInfo createQueueToken(WaitingQueueCommand.CreateToken command);
    Optional<WaitingQueueTokenInfo> findQueueToken(WaitingQueueQuery.GetToken query);
    Long findWaitingTokenOrder(WaitingQueueQuery.GetOrder query);
    Long findActivateQueueTokenCount(TokenStatus tokenStatus);
    List<WaitingQueueToken> findQueuesByStatusAndSize(WaitingQueueQuery.GetQueues query);
    void expireQueueToken(WaitingQueueCommand.ExpireToken command);
    void updateAll(WaitingQueueCommand.UpdateBatch command);
    void deleteAll();
}

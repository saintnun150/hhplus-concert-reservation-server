package org.lowell.concert.domain.waitingqueue.repository;

import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;

import java.util.List;
import java.util.Optional;

public interface WaitingQueueRepository {
    WaitingQueueTokenInfo createQueueToken(WaitingQueueCommand.Create command);
    Optional<WaitingQueueTokenInfo> findWaitingQueueToken(WaitingQueueQuery.GetToken query);
    Optional<WaitingQueueTokenInfo> findActivateQueueToken(WaitingQueueQuery.GetToken query);
    Long findWaitingTokenOrder(WaitingQueueQuery.GetToken query);
    Long findTokenCount(TokenStatus tokenStatus);
    void expireQueueToken(WaitingQueueCommand.ExpireToken command);
    void activateWaitingToken(WaitingQueueQuery.GetQueues query);
}

package org.lowell.apps.waitingqueue.domain.repository;

import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueCommand;
import org.lowell.apps.waitingqueue.domain.dto.WaitingQueueQuery;
import org.lowell.apps.waitingqueue.domain.model.TokenStatus;
import org.lowell.apps.waitingqueue.domain.model.WaitingQueueTokenInfo;

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

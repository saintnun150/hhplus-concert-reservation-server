package org.lowell.concert.domain.waitingqueue.repository;

import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueTokenCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueTokenQuery;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;

public interface WaitingQueueTokenRepository {
    WaitingQueueTokenInfo createQueueToken(WaitingQueueTokenCommand.Create command);
    WaitingQueueTokenInfo getQueueToken(Long tokenId);
    WaitingQueueTokenInfo getQueueToken(String token);
    int getTokenCountByStatus(TokenStatus status);
    int getTokenOrder(WaitingQueueTokenQuery.Order query);

}

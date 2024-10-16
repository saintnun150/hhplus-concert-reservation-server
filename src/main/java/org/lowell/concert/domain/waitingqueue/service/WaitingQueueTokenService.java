package org.lowell.concert.domain.waitingqueue.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueTokenCommand;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueTokenQuery;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueTokenErrorCode;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueTokenException;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
import org.lowell.concert.domain.waitingqueue.repository.WaitingQueueTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WaitingQueueTokenService {
    private static final int ACTIVATE_CAPACITY = 50;
    private final WaitingQueueTokenRepository tokenRepository;

    public WaitingQueueTokenInfo createToken() {
        WaitingQueueTokenCommand.Create command = new WaitingQueueTokenCommand.Create(UUID.randomUUID().toString(),
                                                                                      TokenStatus.WAITING);
        return tokenRepository.createQueueToken(command);
    }

    public TokenStatus getCreatingTokenStatus() {
        int count = tokenRepository.getTokenCountByStatus(TokenStatus.ACTIVATE);
        return count < ACTIVATE_CAPACITY ? TokenStatus.ACTIVATE : TokenStatus.WAITING;
    }

    public int getTokenOrder(String token) {
        WaitingQueueTokenInfo tokenInfo = tokenRepository.getQueueToken(token);
        if (tokenInfo == null) {
            throw new WaitingQueueTokenException(WaitingQueueTokenErrorCode.NOT_FOUND_TOKEN);
        }
        tokenInfo.validateForWaiting();
        WaitingQueueTokenQuery.Order query = new WaitingQueueTokenQuery.Order(tokenInfo.getTokenId(),
                                                                              tokenInfo.getTokenStatus());
        return tokenRepository.getTokenOrder(query);
    }
}

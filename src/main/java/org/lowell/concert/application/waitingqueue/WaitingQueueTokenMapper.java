package org.lowell.concert.application.waitingqueue;

import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
import org.lowell.concert.infra.db.waitingqueue.WaitingQueueTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class WaitingQueueTokenMapper {

    public WaitingQueueTokenInfo toPojo(WaitingQueueTokenEntity entity) {
        if (entity == null) {
            return null;
        }
        return WaitingQueueTokenInfo.builder()
                                    .tokenId(entity.getTokenId())
                                    .token(entity.getToken())
                                    .createdAt(entity.getCreatedAt())
                                    .updatedAt(entity.getUpdatedAt())
                                    .expiresAt(entity.getExpiresAt())
                                    .build();
    }
}

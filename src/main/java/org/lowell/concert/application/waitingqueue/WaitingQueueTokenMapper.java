package org.lowell.concert.application.waitingqueue;

import org.lowell.concert.domain.waitingqueue.model.WaitingQueueTokenInfo;
import org.lowell.concert.infra.db.waitingqueue.WaitingQueueTokenEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public List<WaitingQueueTokenInfo> toPojoList(List<WaitingQueueTokenEntity> entities) {
        if (entities == null) {
            return null;
        }
        List<WaitingQueueTokenInfo> items = new ArrayList<>();
        for (WaitingQueueTokenEntity entity : entities) {
            items.add(toPojo(entity));
        }
        return items;
    }
}

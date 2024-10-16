package org.lowell.concert.application.concert;

import org.lowell.concert.domain.concert.model.ConcertInfo;
import org.lowell.concert.infra.db.concert.entity.ConcertEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConcertMapper {

    public ConcertInfo toPojo(ConcertEntity entity) {
        if (entity == null) {
            return null;
        }
        return ConcertInfo.builder()
                          .concertId(entity.getConcertId())
                          .name(entity.getName())
                          .createdAt(entity.getCreatedAt())
                          .updatedAt(entity.getUpdatedAt())
                          .deletedAt(entity.getDeletedAt())
                          .build();
    }

    public List<ConcertInfo> toPojoList(List<ConcertEntity> entities) {
        if (entities == null) {
            return null;
        }
        List<ConcertInfo> items = new ArrayList<>();
        for (ConcertEntity entity : entities) {
            items.add(toPojo(entity));
        }
        return items;
    }
}

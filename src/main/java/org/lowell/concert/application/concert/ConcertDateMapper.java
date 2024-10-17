package org.lowell.concert.application.concert;

import org.lowell.concert.domain.concert.model.ConcertDateInfo;
import org.lowell.concert.infra.db.concert.entity.ConcertDateEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConcertDateMapper {

    public ConcertDateInfo toPojo(ConcertDateEntity entity) {
        if (entity == null) {
            return null;
        }
        return ConcertDateInfo.builder()
                              .concertDateId(entity.getConcertDateId())
                              .concertId(entity.getConcertId())
                              .concertDate(entity.getConcertDate())
                              .beginTime(entity.getBeginTime())
                              .endTime(entity.getEndTime())
                              .createdAt(entity.getCreatedAt())
                              .updatedAt(entity.getUpdatedAt())
                              .deletedAt(entity.getDeletedAt())
                              .build();
    }

    public List<ConcertDateInfo> toPojoList(List<ConcertDateEntity> entities) {
        if (entities == null) {
            return null;
        }
        List<ConcertDateInfo> items = new ArrayList<>();
        for (ConcertDateEntity entity : entities) {
            items.add(toPojo(entity));
        }
        return items;
    }
}

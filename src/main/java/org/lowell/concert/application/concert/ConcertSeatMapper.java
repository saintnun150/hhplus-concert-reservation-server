package org.lowell.concert.application.concert;

import org.lowell.concert.domain.concert.model.ConcertSeatInfo;
import org.lowell.concert.infra.db.concert.entity.ConcertSeatEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConcertSeatMapper {

    public ConcertSeatInfo toPojo(ConcertSeatEntity entity) {
        if (entity == null) {
            return null;
        }
        return ConcertSeatInfo.builder()
                              .seatId(entity.getSeatId())
                              .concertDateId(entity.getConcertDateId())
                              .seatNo(entity.getSeatNo())
                              .status(entity.getStatus())
                              .price(entity.getPrice())
                              .tempReservedAt(entity.getTempReservedAt())
                              .reservedAt(entity.getReservedAt())
                              .build();
    }

    public List<ConcertSeatInfo> toPojoList(List<ConcertSeatEntity> entities) {
        if (entities == null) {
            return null;
        }
        List<ConcertSeatInfo> items = new ArrayList<>();
        for (ConcertSeatEntity entity : entities) {
            items.add(toPojo(entity));
        }
        return items;
    }
}

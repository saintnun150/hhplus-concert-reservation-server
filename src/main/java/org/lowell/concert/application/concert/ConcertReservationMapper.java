package org.lowell.concert.application.concert;

import org.lowell.concert.domain.concert.model.ConcertReservationInfo;
import org.lowell.concert.infra.db.concert.entity.ConcertReservationEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConcertReservationMapper {

    public ConcertReservationInfo toPojo(ConcertReservationEntity entity) {
        if (entity == null) {
            return null;
        }
        return ConcertReservationInfo.builder()
                                     .reservationId(entity.getReservationId())
                                     .seatId(entity.getSeatId())
                                     .userId(entity.getUserId())
                                     .status(entity.getStatus())
                                     .createdAt(entity.getCreatedAt())
                                     .updatedAt(entity.getUpdatedAt())
                                     .reservedAt(entity.getReservedAt())
                                     .build();
    }

    public List<ConcertReservationInfo> toPojoList(List<ConcertReservationEntity> entities) {
        if (entities == null) {
            return null;
        }
        List<ConcertReservationInfo> items = new ArrayList<>();
        for (ConcertReservationEntity entity : entities) {
            items.add(toPojo(entity));
        }
        return items;
    }
}

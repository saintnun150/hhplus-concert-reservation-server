package org.lowell.apps.concert.interfaces.mapper;

import org.lowell.apps.concert.application.ConcertInfo;
import org.lowell.apps.concert.interfaces.api.ConcertResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcertResponseMapper {
    public List<ConcertResponse.Info> toConcertInfoResponseList(List<ConcertInfo.Info> items) {
        return items.stream()
                    .map(ConcertResponse.Info::of)
                    .toList();
    }

    public List<ConcertResponse.ScheduleInfo> toScheduleInfoResponseList(List<ConcertInfo.ScheduleInfo> items) {
        return items.stream()
                    .map(ConcertResponse.ScheduleInfo::of)
                    .toList();

    }

    public List<ConcertResponse.SeatInfo> toSeatInfoResponseList(List<ConcertInfo.SeatInfo> items) {
        return items.stream()
                    .map(ConcertResponse.SeatInfo::of)
                    .toList();

    }

    public ConcertResponse.ReservationInfo toReservationInfoResponse(ConcertInfo.ReservationInfo item) {
        return ConcertResponse.ReservationInfo.of(item);
    }
}

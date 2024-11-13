package org.lowell.concert.interfaces.api.concert;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.concert.ConcertFacade;
import org.lowell.concert.application.concert.ConcertInfo;
import org.lowell.concert.interfaces.api.common.support.ApiResponse;
import org.lowell.concert.interfaces.api.concert.mapper.ConcertResponseMapper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
public class ConcertController implements ConcertApiDocs {
    private final ConcertFacade concertFacade;
    private final ConcertResponseMapper mapper;

    @SecurityRequirement(name = "queueToken")
    @GetMapping
    public ApiResponse<List<ConcertResponse.Info>> getConcerts(@RequestHeader("X-QUEUE-TOKEN") String token, ConcertRequest.GetConcert dto) {
        List<ConcertInfo.Info> concerts = concertFacade.getConcerts(dto.getConcertName(),
                                                                    dto.getFrom(),
                                                                    dto.getTo());
        return ApiResponse.createOk(mapper.toConcertInfoResponseList(concerts));
    }

    @SecurityRequirement(name = "queueToken")
    @GetMapping("/{concertId}/schedules")
    public ApiResponse<List<ConcertResponse.ScheduleInfo>> getSchedules(@PathVariable Long concertId,
                                                                        @RequestHeader("X-QUEUE-TOKEN") String token,
                                                                        ConcertRequest.GetSchedules dto) {
        List<ConcertInfo.ScheduleInfo> schedules = concertFacade.getConcertSchedule(concertId, dto.getFrom(), dto.getTo());
        return ApiResponse.createOk(mapper.toScheduleInfoResponseList(schedules));
    }

    @SecurityRequirement(name = "queueToken")
    @GetMapping("/{concertId}/schedules/{scheduleId}/seats")
    public ApiResponse<List<ConcertResponse.SeatInfo>> getSeats(@PathVariable Long concertId,
                                                                @PathVariable Long scheduleId,
                                                                @RequestHeader("X-QUEUE-TOKEN") String token) {
        List<ConcertInfo.SeatInfo> concertSeats = concertFacade.getAvailableConcertSeats(scheduleId, LocalDateTime.now());
        return ApiResponse.createOk(mapper.toSeatInfoResponseList(concertSeats));
    }

    @SecurityRequirement(name = "queueToken")
    @PostMapping("/{concertId}/schedules/{scheduleId}/reservations")
    public ApiResponse<ConcertResponse.ReservationInfo> createConcertReservation(@PathVariable Long concertId,
                                                                                 @PathVariable Long scheduleId,
                                                                                 ConcertRequest.Reservation request,
                                                                                 @RequestHeader("X-QUEUE-TOKEN") String token) {

        ConcertInfo.ReservationInfo reservationInfo = concertFacade.reserveConcertSeat(request.getSeatId(), request.getUserId());
        ConcertResponse.ReservationInfo response = mapper.toReservationInfoResponse(reservationInfo);
        return ApiResponse.createOk(response);
    }


}

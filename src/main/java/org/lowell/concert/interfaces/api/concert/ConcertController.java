package org.lowell.concert.interfaces.api.concert;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.concert.ConcertFacade;
import org.lowell.concert.application.concert.ConcertInfo;
import org.lowell.concert.interfaces.api.common.support.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
public class ConcertController implements ConcertApiDocs {
    private final ConcertFacade concertFacade;

    @SecurityRequirement(name = "queueToken")
    @GetMapping
    public ApiResponse<List<ConcertResponse.Info>> getConcerts(@RequestHeader("X-QUEUE-TOKEN") String token) {
        return ApiResponse.createOk(List.of(ConcertResponse.Info.builder()
                                                                .concertId(10L)
                                                                .concertName("싸이 콘서트")
                                                                .createdAt(LocalDate.now())
                                                                .build()));
    }

    @SecurityRequirement(name = "queueToken")
    @GetMapping("/{concertId}/schedules")
    public ApiResponse<List<ConcertResponse.ScheduleInfo>> getSchedules(@PathVariable Long concertId,
                                                                        ConcertRequest.SearchDate request,
                                                                        @RequestHeader("X-QUEUE-TOKEN") String token) {
        List<ConcertInfo.ScheduleInfo> schedules = concertFacade.getConcertSchedule(concertId, LocalDateTime.now());
        List<ConcertResponse.ScheduleInfo> response = schedules.stream()
                                                               .map(schedule -> ConcertResponse.ScheduleInfo.builder()
                                                                                                            .scheduleId(schedule.getScheduleId())
                                                                                                            .scheduleDate(schedule.getScheduleDate())
                                                                                                            .startTime(schedule.getBeginTime())
                                                                                                            .endTime(schedule.getEndTime())
                                                                                                            .build())
                                                               .toList();
        return ApiResponse.createOk(response);
    }

    @SecurityRequirement(name = "queueToken")
    @GetMapping("/{concertId}/schedules/{scheduleId}/seats")
    public ApiResponse<List<ConcertResponse.SeatInfo>> getSeats(@PathVariable Long concertId,
                                                                @PathVariable Long scheduleId,
                                                                @RequestHeader("X-QUEUE-TOKEN") String token) {
        List<ConcertInfo.SeatInfo> concertSeats = concertFacade.getAvailableConcertSeats(scheduleId, LocalDateTime.now());
        List<ConcertResponse.SeatInfo> response = concertSeats.stream()
                                                              .map(seat -> ConcertResponse.SeatInfo.builder()
                                                                                                   .seatId(seat.getSeatId())
                                                                                                   .seatNo(seat.getSeatNo())
                                                                                                   .price(seat.getPrice())
                                                                                                   .build())
                                                              .toList();
        return ApiResponse.createOk(response);
    }

    @SecurityRequirement(name = "queueToken")
    @PostMapping("/{concertId}/schedules/{scheduleId}/reservations")
    public ApiResponse<ConcertResponse.ReservationInfo> createConcertReservation(@PathVariable Long concertId,
                                                                                 @PathVariable Long scheduleId,
                                                                                 ConcertRequest.Reservation request,
                                                                                 @RequestHeader("X-QUEUE-TOKEN") String token) {

        ConcertInfo.ReservationInfo reservationInfo = concertFacade.reserveConcertSeat(request.getSeatId(), request.getUserId());
        ConcertResponse.ReservationInfo response = ConcertResponse.ReservationInfo.builder()
                                                                                  .reservationId(reservationInfo.getReservationId())
                                                                                  .seatNo(reservationInfo.getSeatNo())
                                                                                  .userId(reservationInfo.getUserId())
                                                                                  .status(reservationInfo.getStatus())
                                                                                  .createdAt(reservationInfo.getCreatedAt())
                                                                                  .build();
        return ApiResponse.createOk(response);
    }


}

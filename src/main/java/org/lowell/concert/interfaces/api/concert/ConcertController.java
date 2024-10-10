package org.lowell.concert.interfaces.api.concert;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lowell.concert.interfaces.api.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/concerts")
public class ConcertController implements ConcertApiDocs {

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
    @GetMapping("/{concertId}/dates")
    public ApiResponse<List<ConcertResponse.DateInfo>> getConcertDates(@PathVariable Long concertId,
                                                                       ConcertRequest.SearchDate request,
                                                                       @RequestHeader("X-QUEUE-TOKEN") String token) {
        return ApiResponse.createOk(List.of(ConcertResponse.DateInfo.builder()
                                                                    .concertDateId(1L)
                                                                    .concertDate(LocalDateTime.now())
                                                                    .startTime(LocalDateTime.now().plusHours(1L))
                                                                    .endTime(LocalDateTime.now().plusHours(2L))
                                                                    .build()));
    }

    @SecurityRequirement(name = "queueToken")
    @GetMapping("/{concertId}/dates/{concertDateId}/seats")
    public ApiResponse<List<ConcertResponse.SeatInfo>> getConcertDates(@PathVariable Long concertId,
                                                                       @PathVariable Long concertDateId,
                                                                       @RequestHeader("X-QUEUE-TOKEN") String token) {
        return ApiResponse.createOk(List.of(ConcertResponse.SeatInfo.builder()
                                                                    .seatId(1L)
                                                                    .seatNo(5L)
                                                                    .price(50_000L)
                                                                    .build(),
                                            ConcertResponse.SeatInfo.builder()
                                                                    .seatId(2L)
                                                                    .seatNo(3L)
                                                                    .price(30_000L)
                                                                    .build()));
    }

    @SecurityRequirement(name = "queueToken")
    @PostMapping("/{concertId}/dates/{concertDateId}/reservations")
    public ApiResponse<ConcertResponse.ReservationInfo> createConcertReservation(@PathVariable Long concertId,
                                                                                 @PathVariable Long concertDateId,
                                                                                 ConcertRequest.Reservation request,
                                                                                 @RequestHeader("X-QUEUE-TOKEN") String token) {
        return ApiResponse.createOk(ConcertResponse.ReservationInfo.builder()
                                                                   .concertReservationId(10L)
                                                                   .build());
    }


}

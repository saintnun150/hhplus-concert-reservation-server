package org.lowell.concert.interfaces.api.concert;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.lowell.concert.interfaces.api.common.support.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "콘서트 API", description = "콘서트 관련 API")
public interface ConcertApiDocs {

    @Operation(summary = "콘서트 목록 조회", description = "콘서트 목록을 조회한다.")
    ApiResponse<List<ConcertResponse.Info>> getConcerts(@Parameter(hidden = true) String token);

    @Operation(summary = "예약 가능한 콘서트 날짜 조회", description = "예약 가능한 콘서트 날짜 목록을 반환한다.")
    ApiResponse<List<ConcertResponse.ScheduleInfo>> getSchedules(@PathVariable Long concertId,
                                                                    ConcertRequest.SearchDate request,
                                                                    @Parameter(hidden = true) String token);

    @Operation(summary = "예약 가능한 좌석 조회", description = "예약 가능한 좌석 목록을 반환한다.")
    ApiResponse<List<ConcertResponse.SeatInfo>> getSeats(@PathVariable Long concertId,
                                                                @PathVariable Long scheduleId,
                                                                @Parameter(hidden = true) String token);

    @Operation(summary = "좌석 예약", description = "콘서트 날짜 Id와 좌석 정보를 통해 좌석을 예약 후 예약 정보를 반환한다.")
    ApiResponse<ConcertResponse.ReservationInfo> createConcertReservation(@PathVariable Long concertId,
                                                                          @PathVariable Long scheduleId,
                                                                          ConcertRequest.Reservation request,
                                                                          @Parameter(hidden = true) String token);

}

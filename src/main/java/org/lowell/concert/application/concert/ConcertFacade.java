package org.lowell.concert.application.concert;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.concert.dto.ConcertReservationCommand;
import org.lowell.concert.domain.concert.dto.ConcertScheduleQuery;
import org.lowell.concert.domain.concert.dto.ConcertSeatQuery;
import org.lowell.concert.domain.concert.model.Concert;
import org.lowell.concert.domain.concert.model.ConcertReservation;
import org.lowell.concert.domain.concert.model.ConcertSchedule;
import org.lowell.concert.domain.concert.model.ConcertSeat;
import org.lowell.concert.domain.concert.service.ConcertReservationService;
import org.lowell.concert.domain.concert.service.ConcertScheduleService;
import org.lowell.concert.domain.concert.service.ConcertSeatService;
import org.lowell.concert.domain.concert.service.ConcertService;
import org.lowell.concert.domain.user.model.User;
import org.lowell.concert.domain.user.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {
    private final ConcertService concertService;
    private final ConcertScheduleService concertScheduleService;
    private final ConcertSeatService concertSeatService;
    private final ConcertReservationService concertReservationService;

    private final UserService userService;

    public List<ConcertInfo.ScheduleInfo> getConcertSchedule(Long concertId, LocalDateTime scheduleDate) {
        Concert concert = concertService.getConcert(concertId);
        List<ConcertSchedule> concertSchedules = concertScheduleService.getConcertSchedules(new ConcertScheduleQuery.SearchList(concert.getConcertId(), scheduleDate));
        List<ConcertInfo.ScheduleInfo> scheduleInfoList =
                concertSchedules.stream()
                                .map(schedule -> new ConcertInfo.ScheduleInfo(schedule.getScheduleId(),
                                                                              schedule.getConcertId(),
                                                                              schedule.getScheduleDate(),
                                                                              schedule.getBeginTime(),
                                                                              schedule.getEndTime(),
                                                                              schedule.getCreatedAt()))


                                .toList();
        return scheduleInfoList;
    }

    public List<ConcertInfo.SeatInfo> getAvailableConcertSeats(Long concertScheduleId) {
        ConcertSchedule schedule = concertScheduleService.getConcertSchedule(concertScheduleId);
        List<ConcertSeat> availableSeats = concertSeatService.getAvailableSeats(new ConcertSeatQuery.SearchList(schedule.getScheduleId()));
        List<ConcertInfo.SeatInfo> seatInfoList =
                availableSeats.stream()
                              .map(seat -> new ConcertInfo.SeatInfo(seat.getSeatId(),
                                                                    seat.getConcertScheduleId(),
                                                                    seat.getSeatNo(),
                                                                    seat.getStatus(),
                                                                    seat.getPrice()))
                              .toList();
        return seatInfoList;
    }

    @Transactional
    public ConcertInfo.ReservationInfo reserveConcertSeat(Long seatId, Long userId) {
        User user = userService.getUser(userId);

        ConcertSeat seat = concertSeatService.getConcertSeatWithLock(new ConcertSeatQuery.Search(seatId));
        seat.checkAvailableSeat(LocalDateTime.now(), ConcertPolicy.TEMP_RESERVED_MINUTES);
        seat.reserveSeatTemporarily();

        ConcertReservation reservation = concertReservationService.createConcertReservation(new ConcertReservationCommand.Create(seat.getSeatId(),
                                                                                                                                 user.getUserId()));
        return new ConcertInfo.ReservationInfo(reservation.getReservationId(),
                                               seat.getSeatNo(),
                                               user.getUserId(),
                                               reservation.getStatus(),
                                               reservation.getCreatedAt());
    }


}

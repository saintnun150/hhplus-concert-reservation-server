package org.lowell.concert.application.concert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.concert.dto.ConcertQuery;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class ConcertFacade {
    private final ConcertService concertService;
    private final ConcertScheduleService concertScheduleService;
    private final ConcertSeatService concertSeatService;
    private final ConcertReservationService concertReservationService;

    private final UserService userService;

    public List<ConcertInfo.Info> getConcerts(String concertName, LocalDateTime from, LocalDateTime to) {
        return concertService.getConcerts(new ConcertQuery.SearchList(concertName, from, to))
                             .stream()
                             .map(concert -> ConcertInfo.Info.of(concert.getConcertId(), concert.getName(), concert.getOpenedAt()))
                             .toList();
    }

    public List<ConcertInfo.ScheduleInfo> getConcertSchedule(Long concertId, LocalDateTime from, LocalDateTime to) {
        Concert concert = concertService.getConcert(concertId);
        List<ConcertSchedule> concertSchedules = concertScheduleService.getConcertSchedules(new ConcertScheduleQuery.SearchList(concert.getConcertId(), from, to));
        return concertSchedules.stream()
                               .map(schedule -> ConcertInfo.ScheduleInfo.of(schedule.getScheduleId(),
                                                                            schedule.getConcertId(),
                                                                            schedule.getScheduleDate(),
                                                                            schedule.getBeginTime(),
                                                                            schedule.getEndTime(),
                                                                            schedule.getCreatedAt()))
                               .toList();
    }

    public List<ConcertInfo.SeatInfo> getAvailableConcertSeats(Long concertScheduleId, LocalDateTime now) {
        ConcertSchedule schedule = concertScheduleService.getConcertSchedule(concertScheduleId);
        List<ConcertSeat> availableSeats = concertSeatService.getAvailableSeats(new ConcertSeatQuery.SearchList(schedule.getScheduleId(), now));
        return availableSeats.stream()
                             .map(seat -> ConcertInfo.SeatInfo.of(seat.getSeatId(),
                                                                  seat.getConcertScheduleId(),
                                                                  seat.getSeatNo(),
                                                                  seat.getStatus(),
                                                                  seat.getPrice()))
                             .toList();
    }

    @Transactional
    public ConcertInfo.ReservationInfo reserveConcertSeat(Long seatId, Long userId) {
        User user = userService.getUser(userId);
        ConcertSeat seat = concertSeatService.getConcertSeatWithLock(new ConcertSeatQuery.Search(seatId));
        seat.checkReservableSeat(LocalDateTime.now(), ConcertPolicy.TEMP_RESERVED_SEAT_MINUTES);
        seat.reserveSeatTemporarily(LocalDateTime.now());

        ConcertReservation reservation = concertReservationService.createConcertReservation(new ConcertReservationCommand.Create(seat.getSeatId(),
                                                                                                                                 user.getUserId()));
        return ConcertInfo.ReservationInfo.of(reservation.getReservationId(),
                                              seat.getSeatNo(),
                                              user.getUserId(),
                                              reservation.getStatus(),
                                              reservation.getCreatedAt());
    }


}

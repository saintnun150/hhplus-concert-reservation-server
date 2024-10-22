package org.lowell.concert.domain.concert.repository;

import org.lowell.concert.domain.concert.dto.ConcertScheduleCommand;
import org.lowell.concert.domain.concert.model.ConcertSchedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertScheduleRepository {
    void createConcertDate(ConcertScheduleCommand.Create command);
    Optional<ConcertSchedule> getConcertSchedule(long concertScheduleId);
    List<ConcertSchedule> getConcertDates();
    List<ConcertSchedule> getConcertDates(long concertId);
    List<ConcertSchedule> getConcertDates(LocalDateTime scheduleDate);
    List<ConcertSchedule> getConcertDates(long concertId, LocalDateTime concertDate);
}

package org.lowell.concert.domain.concert.repository;

import org.lowell.concert.domain.concert.dto.ConcertScheduleCommand;
import org.lowell.concert.domain.concert.dto.ConcertScheduleQuery;
import org.lowell.concert.domain.concert.model.ConcertSchedule;

import java.util.List;
import java.util.Optional;

public interface ConcertScheduleRepository {
    void createConcertSchedule(ConcertScheduleCommand.Create command);
    Optional<ConcertSchedule> getConcertSchedule(long concertScheduleId);
    List<ConcertSchedule> getConcertSchedules(ConcertScheduleQuery.SearchList query);
}

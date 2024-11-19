package org.lowell.apps.concert.domain.repository;

import org.lowell.apps.concert.domain.dto.ConcertScheduleCommand;
import org.lowell.apps.concert.domain.dto.ConcertScheduleQuery;
import org.lowell.apps.concert.domain.model.ConcertSchedule;

import java.util.List;
import java.util.Optional;

public interface ConcertScheduleRepository {
    void createConcertSchedule(ConcertScheduleCommand.Create command);
    Optional<ConcertSchedule> getConcertSchedule(long concertScheduleId);
    List<ConcertSchedule> getConcertSchedules(ConcertScheduleQuery.SearchList query);
}

package org.lowell.concert.domain.concert.repository;

import org.lowell.concert.domain.concert.dto.ConcertDateCommand;
import org.lowell.concert.domain.concert.model.ConcertDateInfo;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertDateRepository {
    void createConcertDate(ConcertDateCommand.Create command);
    ConcertDateInfo getConcertDate(long concertDateId);
    List<ConcertDateInfo> getConcertDates();
    List<ConcertDateInfo> getConcertDates(long concertId);
    List<ConcertDateInfo> getConcertDates(LocalDateTime concertDate);
    List<ConcertDateInfo> getConcertDates(long concertId, LocalDateTime concertDate);

}

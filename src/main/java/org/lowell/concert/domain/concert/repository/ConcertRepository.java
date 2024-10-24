package org.lowell.concert.domain.concert.repository;

import org.lowell.concert.domain.concert.dto.ConcertCommand;
import org.lowell.concert.domain.concert.model.ConcertInfo;

import java.util.List;

public interface ConcertRepository {
    void createConcert(ConcertCommand.Create command);
    ConcertInfo getConcert(long concertId);
    List<ConcertInfo> getConcerts();

}

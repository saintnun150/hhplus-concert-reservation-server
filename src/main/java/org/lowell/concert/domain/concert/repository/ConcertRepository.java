package org.lowell.concert.domain.concert.repository;

import org.lowell.concert.domain.concert.dto.ConcertCommand;
import org.lowell.concert.domain.concert.model.Concert;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    void createConcert(ConcertCommand.Create command);
    Optional<Concert> getConcert(long concertId);
    List<Concert> getConcerts();

}

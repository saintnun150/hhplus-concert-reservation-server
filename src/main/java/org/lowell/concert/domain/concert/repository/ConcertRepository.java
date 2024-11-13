package org.lowell.concert.domain.concert.repository;

import org.lowell.concert.domain.concert.dto.ConcertQuery;
import org.lowell.concert.domain.concert.model.Concert;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    Optional<Concert> getConcert(long concertId);
    List<Concert> getConcerts(ConcertQuery.SearchList query);

}

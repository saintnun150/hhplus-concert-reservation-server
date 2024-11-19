package org.lowell.apps.concert.domain.repository;

import org.lowell.apps.concert.domain.dto.ConcertQuery;
import org.lowell.apps.concert.domain.model.Concert;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    Optional<Concert> getConcert(long concertId);
    List<Concert> getConcerts(ConcertQuery.SearchList query);

}

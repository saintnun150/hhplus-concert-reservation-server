package org.lowell.concert.domain.concert.repository;

import org.lowell.concert.domain.concert.dto.ConcertReservationCommand;
import org.lowell.concert.domain.concert.dto.ConcertReservationQuery;
import org.lowell.concert.domain.concert.model.ConcertReservation;

import java.util.List;
import java.util.Optional;

public interface ConcertReservationRepository {
    ConcertReservation createConcertReservation(ConcertReservationCommand.Create command);
    Optional<ConcertReservation> getConcertReservation(ConcertReservationQuery.Search query);
    Optional<ConcertReservation> getConcertReservationWithLock(ConcertReservationQuery.Search query);
    List<ConcertReservation> getConcertReservations(ConcertReservationQuery.SearchList query);
    void deleteAll();
}

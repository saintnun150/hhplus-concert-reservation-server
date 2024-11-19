package org.lowell.apps.concert.domain.repository;

import org.lowell.apps.concert.domain.dto.ConcertReservationCommand;
import org.lowell.apps.concert.domain.dto.ConcertReservationQuery;
import org.lowell.apps.concert.domain.model.ConcertReservation;

import java.util.List;
import java.util.Optional;

public interface ConcertReservationRepository {
    ConcertReservation createConcertReservation(ConcertReservationCommand.Create command);
    Optional<ConcertReservation> getConcertReservation(ConcertReservationQuery.Search query);
    Optional<ConcertReservation> getConcertReservationWithLock(ConcertReservationQuery.Search query);
    List<ConcertReservation> getConcertReservations(ConcertReservationQuery.SearchList query);
    void deleteAll();
}

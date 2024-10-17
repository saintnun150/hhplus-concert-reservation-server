package org.lowell.concert.domain.concert.repository;

import org.lowell.concert.domain.concert.dto.ConcertReservationCommand;
import org.lowell.concert.domain.concert.dto.ConcertReservationQuery;
import org.lowell.concert.domain.concert.model.ConcertReservationInfo;

import java.util.List;

public interface ConcertReservationRepository {
    void createConcertReservation(ConcertReservationCommand.Create command);
    ConcertReservationInfo getConcertReservation(ConcertReservationQuery.Search query);
    List<ConcertReservationInfo> getConcertReservations(ConcertReservationQuery.SearchList query);
    void updateConcertReservation(ConcertReservationCommand.Update command);

}

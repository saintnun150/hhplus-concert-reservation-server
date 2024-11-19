package org.lowell.apps.concert.domain.repository;

import org.lowell.apps.concert.domain.dto.ConcertSeatCommand;
import org.lowell.apps.concert.domain.dto.ConcertSeatQuery;
import org.lowell.apps.concert.domain.model.ConcertSeat;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatRepository {
    Optional<ConcertSeat> getConcertSeat(ConcertSeatQuery.Search query);
    Optional<ConcertSeat> getConcertSeatWithLock(ConcertSeatQuery.Search query);
    List<ConcertSeat> getConcertSeats(ConcertSeatQuery.SearchList query);
    void saveAll(List<ConcertSeat> concertSeats);
    void deleteAll();
    void createConcertSeat(ConcertSeatCommand.Create command);
}

package org.lowell.concert.domain.concert.repository;

import org.lowell.concert.domain.concert.dto.ConcertSeatCommand;
import org.lowell.concert.domain.concert.dto.ConcertSeatQuery;
import org.lowell.concert.domain.concert.model.Concert;
import org.lowell.concert.domain.concert.model.ConcertSeat;

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

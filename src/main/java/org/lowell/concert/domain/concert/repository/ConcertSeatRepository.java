package org.lowell.concert.domain.concert.repository;

import org.lowell.concert.domain.concert.dto.ConcertSeatQuery;
import org.lowell.concert.domain.concert.model.ConcertSeatInfo;

import java.util.List;

public interface ConcertSeatRepository {
    ConcertSeatInfo getConcertSeat(ConcertSeatQuery.Search query);
    List<ConcertSeatInfo> getConcertSeats(ConcertSeatQuery.SearchList query);
}

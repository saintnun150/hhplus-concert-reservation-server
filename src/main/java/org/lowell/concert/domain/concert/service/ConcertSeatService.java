package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.dto.ConcertSeatQuery;
import org.lowell.concert.domain.concert.exception.ConcertSeatErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertSeatException;
import org.lowell.concert.domain.concert.model.ConcertSeatInfo;
import org.lowell.concert.domain.concert.repository.ConcertSeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertSeatService {
    private final ConcertSeatRepository concertSeatRepository;

    public ConcertSeatInfo getConcertSeat(ConcertSeatQuery.Search query) {
        ConcertSeatInfo concertSeat = concertSeatRepository.getConcertSeat(query);
        if (concertSeat == null) {
            throw new ConcertSeatException(ConcertSeatErrorCode.NOT_FOUND_SEAT);
        }
        return concertSeat;
    }

    public List<ConcertSeatInfo> getConcertSeats(ConcertSeatQuery.SearchList query) {
        List<ConcertSeatInfo> concertSeats = concertSeatRepository.getConcertSeats(query);
        if (concertSeats == null) {
            throw new ConcertSeatException(ConcertSeatErrorCode.NOT_FOUND_SEAT);
        }
        return concertSeats;
    }
}

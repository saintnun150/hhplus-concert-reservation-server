package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.dto.ConcertSeatQuery;
import org.lowell.concert.domain.concert.exception.ConcertSeatErrorCode;
import org.lowell.concert.domain.concert.model.ConcertSeat;
import org.lowell.concert.domain.concert.repository.ConcertSeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertSeatService {
    private final ConcertSeatRepository concertSeatRepository;

    public ConcertSeat getConcertSeat(ConcertSeatQuery.Search query) {
        return concertSeatRepository.getConcertSeat(query)
                                    .orElseThrow(()-> DomainException.create(ConcertSeatErrorCode.NOT_FOUND_SEAT));
    }

    public ConcertSeat getConcertSeatWithLock(ConcertSeatQuery.Search query) {
        return concertSeatRepository.getConcertSeatWithLock(query)
                                    .orElseThrow(()-> DomainException.create(ConcertSeatErrorCode.NOT_FOUND_SEAT));
    }

    public List<ConcertSeat> getConcertSeats(ConcertSeatQuery.SearchList query) {
        List<ConcertSeat> concertSeats = concertSeatRepository.getConcertSeats(query);
        if (CollectionUtils.isEmpty(concertSeats)) {
            throw new DomainException(ConcertSeatErrorCode.NOT_FOUND_SEAT);
        }
        return concertSeats;
    }

    public List<ConcertSeat> getAvailableSeats(ConcertSeatQuery.SearchList query) {
        List<ConcertSeat> concertSeats = getConcertSeats(query).stream()
                                                               .filter(ConcertSeat::isEmpty)
                                                               .toList();

        if (CollectionUtils.isEmpty(concertSeats)) {
            throw new DomainException(ConcertSeatErrorCode.NOT_FOUND_AVAILABLE_SEAT);
        }
        return concertSeats;
    }


}

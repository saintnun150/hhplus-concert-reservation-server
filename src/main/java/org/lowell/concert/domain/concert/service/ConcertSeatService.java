package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.ConcertPolicy;
import org.lowell.concert.domain.concert.dto.ConcertSeatCommand;
import org.lowell.concert.domain.concert.dto.ConcertSeatQuery;
import org.lowell.concert.domain.concert.exception.ConcertSeatError;
import org.lowell.concert.domain.concert.model.ConcertSeat;
import org.lowell.concert.domain.concert.repository.ConcertSeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertSeatService {
    private final ConcertSeatRepository concertSeatRepository;

    @Transactional
    public void createConcertSeat(ConcertSeatCommand.Create command) {
        concertSeatRepository.createConcertSeat(command);
    }

    public ConcertSeat getConcertSeat(ConcertSeatQuery.Search query) {
        return concertSeatRepository.getConcertSeat(query)
                                    .orElseThrow(()-> DomainException.create(ConcertSeatError.NOT_FOUND_SEAT, DomainException.createPayload(query)));
    }

    public ConcertSeat getConcertSeatWithLock(ConcertSeatQuery.Search query) {
        return concertSeatRepository.getConcertSeatWithLock(query)
                                    .orElseThrow(()-> DomainException.create(ConcertSeatError.NOT_FOUND_SEAT, DomainException.createPayload(query)));
    }

    public List<ConcertSeat> getConcertSeats(ConcertSeatQuery.SearchList query) {
        List<ConcertSeat> concertSeats = concertSeatRepository.getConcertSeats(query);
        if (CollectionUtils.isEmpty(concertSeats)) {
            throw DomainException.create(ConcertSeatError.NOT_FOUND_SEAT, DomainException.createPayload(query));
        }
        return concertSeats;
    }

    public List<ConcertSeat> getAvailableSeats(ConcertSeatQuery.SearchList query) {
        List<ConcertSeat> concertSeats = getConcertSeats(query).stream()
                                                               .filter(seat -> seat.isEmpty() || seat.isTemporaryReserved(query.now(), ConcertPolicy.TEMP_RESERVED_SEAT_MINUTES))
                                                               .toList();
        if (CollectionUtils.isEmpty(concertSeats)) {
            throw DomainException.create(ConcertSeatError.NOT_FOUND_AVAILABLE_SEAT, DomainException.createPayload(query));
        }
        return concertSeats;
    }

}

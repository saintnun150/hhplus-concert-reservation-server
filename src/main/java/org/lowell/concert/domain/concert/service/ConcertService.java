package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.dto.ConcertQuery;
import org.lowell.concert.domain.concert.exception.ConcertError;
import org.lowell.concert.domain.concert.model.Concert;
import org.lowell.concert.domain.concert.repository.ConcertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    public Concert getConcert(long id) {
        return concertRepository.getConcert(id)
                                .orElseThrow(() -> DomainException.create(ConcertError.NOT_FOUND_CONCERT));
    }

    public List<Concert> getConcerts(ConcertQuery.SearchList query) {
        return concertRepository.getConcerts(query);
    }
}

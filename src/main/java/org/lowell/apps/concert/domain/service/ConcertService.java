package org.lowell.apps.concert.domain.service;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.concert.domain.dto.ConcertQuery;
import org.lowell.apps.concert.domain.exception.ConcertError;
import org.lowell.apps.concert.domain.model.Concert;
import org.lowell.apps.concert.domain.repository.ConcertRepository;
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

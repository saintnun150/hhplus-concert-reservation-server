package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.dto.ConcertDateCommand;
import org.lowell.concert.domain.concert.dto.ConcertDateQuery;
import org.lowell.concert.domain.concert.exception.ConcertDateErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertDateException;
import org.lowell.concert.domain.concert.model.ConcertDateInfo;
import org.lowell.concert.domain.concert.repository.ConcertDateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertDateService {
    private final ConcertDateRepository concertDateRepository;

    @Transactional
    public void createConcertDate(ConcertDateCommand.Create command) {
        if (command.concertId() == null) {
            throw new ConcertDateException(ConcertDateErrorCode.INVALID_CONCERT_ID);
        }
        if (command.concertDate() == null) {
            throw new ConcertDateException(ConcertDateErrorCode.INVALID_CONCERT_DATE);
        }
        concertDateRepository.createConcertDate(command);
    }

    public ConcertDateInfo getConcertDate(long concertDateId) {
        ConcertDateInfo concertDateInfo = concertDateRepository.getConcertDate(concertDateId);
        if (concertDateInfo == null) {
            throw new ConcertDateException(ConcertDateErrorCode.NOT_FOUND_CONCERT_DATE);
        }
        return concertDateInfo;
    }

    public List<ConcertDateInfo> getConcertDates(ConcertDateQuery.SearchList query) {
        List<ConcertDateInfo> concertDates;
        if (query == null || (query.concertId() == null && query.concertDate() == null)) {
            concertDates = concertDateRepository.getConcertDates();
        } else {
            if (query.concertId() != null && query.concertDate() != null) {
                concertDates = concertDateRepository.getConcertDates(query.concertId(), query.concertDate());
            } else if (query.concertId() != null) {
                concertDates = concertDateRepository.getConcertDates(query.concertId());
            } else {
                concertDates = concertDateRepository.getConcertDates(query.concertDate());
            }
        }

        if (concertDates == null) {
            throw new ConcertDateException(ConcertDateErrorCode.NOT_FOUND_CONCERT_DATE);
        }
        return concertDates;
    }
}

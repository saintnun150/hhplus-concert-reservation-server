package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.dto.ConcertCommand;
import org.lowell.concert.domain.concert.exception.ConcertErrorCode;
import org.lowell.concert.domain.concert.model.Concert;
import org.lowell.concert.domain.concert.repository.ConcertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    @Transactional
    public void createConcert(ConcertCommand.Create command) {
        if (!StringUtils.hasText(command.name())) {
            throw new DomainException(ConcertErrorCode.INVALID_CONCERT_NAME);
        }
        concertRepository.createConcert(command);
    }

    public Concert getConcert(long id) {
        Concert concert = concertRepository.getConcert(id)
                                           .orElseThrow();
        return concert;
    }

    public List<Concert> getConcerts() {
        List<Concert> concerts = concertRepository.getConcerts();
        if (CollectionUtils.isEmpty(concerts)) {
            throw new DomainException(ConcertErrorCode.NOT_FOUND_CONCERT);
        }
        return concerts;
    }
}

package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.dto.ConcertCommand;
import org.lowell.concert.domain.concert.exception.ConcertErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertException;
import org.lowell.concert.domain.concert.model.ConcertInfo;
import org.lowell.concert.domain.concert.repository.ConcertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    @Transactional
    public void createConcert(ConcertCommand.Create command) {
        if (!StringUtils.hasText(command.name())) {
            throw new ConcertException(ConcertErrorCode.INVALID_CONCERT_NAME);
        }
        concertRepository.createConcert(command);
    }

    public ConcertInfo getConcert(long id) {
        ConcertInfo concertInfo = concertRepository.getConcert(id);
        if (concertInfo == null) {
            throw new ConcertException(ConcertErrorCode.NOT_FOUND_CONCERT);
        }
        return concertInfo;
    }

    public List<ConcertInfo> getConcerts() {
        List<ConcertInfo> concertInfos = concertRepository.getConcerts();
        if (concertInfos == null) {
            throw new ConcertException(ConcertErrorCode.NOT_FOUND_CONCERT);
        }
        return concertInfos;
    }
}

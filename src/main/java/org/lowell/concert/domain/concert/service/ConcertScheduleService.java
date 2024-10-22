package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.dto.ConcertScheduleCommand;
import org.lowell.concert.domain.concert.dto.ConcertScheduleQuery;
import org.lowell.concert.domain.concert.exception.ConcertScheduleErrorCode;
import org.lowell.concert.domain.concert.model.ConcertSchedule;
import org.lowell.concert.domain.concert.repository.ConcertScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertScheduleService {
    private final ConcertScheduleRepository concertScheduleRepository;

    @Transactional
    public void createConcertSchedule(ConcertScheduleCommand.Create command) {
        if (command.concertId() == null) {
            throw new DomainException(ConcertScheduleErrorCode.INVALID_CONCERT_ID);
        }
        if (command.scheduleDate() == null) {
            throw new DomainException(ConcertScheduleErrorCode.INVALID_SCHEDULE_DATE);
        }
        concertScheduleRepository.createConcertDate(command);
    }

    public ConcertSchedule getConcertSchedule(long concertScheduleId) {
        ConcertSchedule schedule = concertScheduleRepository.getConcertSchedule(concertScheduleId)
                                                            .orElseThrow(() -> new DomainException(ConcertScheduleErrorCode.NOT_FOUND_CONCERT_SCHEDULE));
        return schedule;
    }

    public List<ConcertSchedule> getConcertSchedules(ConcertScheduleQuery.SearchList query) {
        List<ConcertSchedule> concertDates;
        if (query == null || (query.concertId() == null && query.scheduleDate() == null)) {
            concertDates = concertScheduleRepository.getConcertDates();
        } else {
            if (query.concertId() != null && query.scheduleDate() != null) {
                concertDates = concertScheduleRepository.getConcertDates(query.concertId(), query.scheduleDate());
            } else if (query.concertId() != null) {
                concertDates = concertScheduleRepository.getConcertDates(query.concertId());
            } else {
                concertDates = concertScheduleRepository.getConcertDates(query.scheduleDate());
            }
        }

        if (concertDates == null) {
            throw new DomainException(ConcertScheduleErrorCode.NOT_FOUND_CONCERT_SCHEDULE);
        }
        return concertDates;
    }
}

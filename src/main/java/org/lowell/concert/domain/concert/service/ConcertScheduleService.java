package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.dto.ConcertScheduleCommand;
import org.lowell.concert.domain.concert.dto.ConcertScheduleQuery;
import org.lowell.concert.domain.concert.exception.ConcertScheduleError;
import org.lowell.concert.domain.concert.model.ConcertSchedule;
import org.lowell.concert.domain.concert.repository.ConcertScheduleRepository;
import org.springframework.cache.annotation.Cacheable;
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
            throw DomainException.create(ConcertScheduleError.INVALID_CONCERT_ID, DomainException.createPayload(command));
        }
        if (command.scheduleDate() == null) {
            throw DomainException.create(ConcertScheduleError.INVALID_SCHEDULE_DATE, DomainException.createPayload(command));
        }
        concertScheduleRepository.createConcertDate(command);
    }

    public ConcertSchedule getConcertSchedule(long concertScheduleId) {
        ConcertSchedule schedule = concertScheduleRepository.getConcertSchedule(concertScheduleId)
                                                            .orElseThrow(() -> DomainException.create(ConcertScheduleError.NOT_FOUND_CONCERT_SCHEDULE, DomainException.createPayload(concertScheduleId)));
        return schedule;
    }

    @Cacheable(value = "concertSchedules", key = "#query.concertId()")
    public List<ConcertSchedule> getConcertSchedulesWithCache(ConcertScheduleQuery.SearchList query) {
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
            throw DomainException.create(ConcertScheduleError.NOT_FOUND_CONCERT_SCHEDULE, DomainException.createPayload(query));
        }
        return concertDates;
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
            throw DomainException.create(ConcertScheduleError.NOT_FOUND_CONCERT_SCHEDULE, DomainException.createPayload(query));
        }
        return concertDates;
    }
}

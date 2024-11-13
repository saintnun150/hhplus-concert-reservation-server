package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.dto.ConcertScheduleCommand;
import org.lowell.concert.domain.concert.dto.ConcertScheduleQuery;
import org.lowell.concert.domain.concert.model.ConcertSchedule;
import org.lowell.concert.domain.concert.repository.ConcertScheduleRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.lowell.concert.domain.concert.exception.ConcertScheduleError.*;

@Service
@RequiredArgsConstructor
public class ConcertScheduleService {
    private final ConcertScheduleRepository concertScheduleRepository;

    @Transactional
    public void createConcertSchedule(ConcertScheduleCommand.Create command) {
        if (command.concertId() == null) {
            throw DomainException.create(INVALID_CONCERT_ID, DomainException.createPayload(command));
        }
        if (command.scheduleDate() == null) {
            throw DomainException.create(INVALID_SCHEDULE_DATE, DomainException.createPayload(command));
        }
        concertScheduleRepository.createConcertSchedule(command);
    }

    public ConcertSchedule getConcertSchedule(long concertScheduleId) {
        return concertScheduleRepository.getConcertSchedule(concertScheduleId)
                                        .orElseThrow(() -> DomainException.create(NOT_FOUND_CONCERT_SCHEDULE,
                                                                                  DomainException.createPayload(concertScheduleId)));
    }

    @Cacheable(value = "concertSchedules", key = "#query.concertId()")
    public List<ConcertSchedule> getConcertSchedulesWithCache(ConcertScheduleQuery.SearchList query) {
        return concertScheduleRepository.getConcertSchedules(query);
    }

    public List<ConcertSchedule> getConcertSchedules(ConcertScheduleQuery.SearchList query) {
        return concertScheduleRepository.getConcertSchedules(query);
    }
}

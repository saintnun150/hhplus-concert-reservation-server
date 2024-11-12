package org.lowell.concert.infra.db.concert.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.dto.ConcertScheduleCommand;
import org.lowell.concert.domain.concert.dto.ConcertScheduleQuery;
import org.lowell.concert.domain.concert.model.ConcertSchedule;
import org.lowell.concert.domain.concert.repository.ConcertScheduleRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertScheduleRepositoryImpl implements ConcertScheduleRepository {
    private final ConcertScheduleJpaRepository jpaRepository;

    @Override
    @Transactional
    public void createConcertSchedule(ConcertScheduleCommand.Create command) {
        ConcertSchedule entity = ConcertSchedule.builder()
                                                .concertId(command.concertId())
                                                .scheduleDate(command.scheduleDate())
                                                .beginTime(command.beginTime())
                                                .endTime(command.endTime())
                                                .createdAt(LocalDateTime.now())
                                                .build();
        jpaRepository.save(entity);
    }

    @Override
    public Optional<ConcertSchedule> getConcertSchedule(long concertScheduleId) {
        return jpaRepository.findById(concertScheduleId);
    }

    @Override
    public List<ConcertSchedule> getConcertSchedules(ConcertScheduleQuery.SearchList query) {
        boolean hasConcertId = query.concertId() != null;
        boolean hasFromAndTo = query.from() != null && query.to() != null;

        if (hasConcertId && hasFromAndTo) {
            return jpaRepository.findAllByConcertIdAndScheduleDateBetween(query.concertId(), query.from(), query.to());
        }
        if (hasConcertId) {
            return jpaRepository.findAllByConcertId(query.concertId());
        }
        if (hasFromAndTo) {
            return jpaRepository.findAllByScheduleDateBetween(query.from(), query.to());
        }
        return jpaRepository.findAll();
    }


}

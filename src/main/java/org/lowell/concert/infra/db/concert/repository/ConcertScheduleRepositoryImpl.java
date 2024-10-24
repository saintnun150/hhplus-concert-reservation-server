package org.lowell.concert.infra.db.concert.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.dto.ConcertScheduleCommand;
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
    public void createConcertDate(ConcertScheduleCommand.Create command) {
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
    public List<ConcertSchedule> getConcertDates() {
        return jpaRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public List<ConcertSchedule> getConcertDates(long concertId) {
        return jpaRepository.findAllByConcertIdAndDeletedAtIsNull(concertId);
    }

    @Override
    public List<ConcertSchedule> getConcertDates(LocalDateTime scheduleDate) {
        return jpaRepository.findAllByScheduleDateAndDeletedAtIsNull(scheduleDate);
    }

    @Override
    public List<ConcertSchedule> getConcertDates(long concertId, LocalDateTime concertDate) {
        return jpaRepository.findAllByConcertIdAndScheduleDateAndDeletedAtIsNull(concertId, concertDate);
    }
}

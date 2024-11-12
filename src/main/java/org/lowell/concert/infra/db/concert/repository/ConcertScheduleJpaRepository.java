package org.lowell.concert.infra.db.concert.repository;

import org.lowell.concert.domain.concert.model.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {
    List<ConcertSchedule> findAllByConcertId(Long concertId);
    List<ConcertSchedule> findAllByConcertIdAndScheduleDateBetween(Long concertId, LocalDateTime from, LocalDateTime to);
    List<ConcertSchedule> findAllByScheduleDateBetween(LocalDateTime from, LocalDateTime to);
}

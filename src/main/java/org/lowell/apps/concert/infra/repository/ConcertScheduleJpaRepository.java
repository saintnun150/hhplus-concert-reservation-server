package org.lowell.apps.concert.infra.repository;

import org.lowell.apps.concert.domain.model.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {
    List<ConcertSchedule> findAllByConcertId(Long concertId);
    List<ConcertSchedule> findAllByConcertIdAndScheduleDateBetween(Long concertId, LocalDateTime from, LocalDateTime to);
    List<ConcertSchedule> findAllByScheduleDateBetween(LocalDateTime from, LocalDateTime to);
}

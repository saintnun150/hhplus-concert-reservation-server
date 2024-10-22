package org.lowell.concert.infra.db.concert.repository;

import org.lowell.concert.domain.concert.model.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {
    List<ConcertSchedule> findAllByDeletedAtIsNull();
    List<ConcertSchedule> findAllByConcertIdAndDeletedAtIsNull(long concertId);
    List<ConcertSchedule> findAllByScheduleDateAndDeletedAtIsNull(LocalDateTime scheduleDate);
    List<ConcertSchedule> findAllByConcertIdAndScheduleDateAndDeletedAtIsNull(long concertId, LocalDateTime scheduleDate);

}

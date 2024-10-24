package org.lowell.concert.infra.db.concert.repository;

import org.lowell.concert.infra.db.concert.entity.ConcertDateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertDateJpaRepository extends JpaRepository<ConcertDateEntity, Long> {
    List<ConcertDateEntity> findAllByDeletedAtIsNull();
    List<ConcertDateEntity> findAllByConcertIdAndDeletedAtIsNull(long concertId);
    List<ConcertDateEntity> findAllByConcertDateAndDeletedAtIsNull(LocalDateTime concertDate);
    List<ConcertDateEntity> findAllByConcertIdAndConcertDateAndDeletedAtIsNull(long concertId, LocalDateTime concertDate);

}

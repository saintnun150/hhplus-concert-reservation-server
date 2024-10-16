package org.lowell.concert.infra.db.concert.repository;

import org.lowell.concert.infra.db.concert.entity.ConcertSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long> {
    ConcertSeatEntity findByConcertDateIdAndSeatNo();

    List<ConcertSeatEntity> findAllByConcertDateId(Long concertDateId);
}

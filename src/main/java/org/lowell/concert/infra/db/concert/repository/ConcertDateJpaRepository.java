package org.lowell.concert.infra.db.concert.repository;

import org.lowell.concert.infra.db.concert.entity.ConcertDateEntity;
import org.lowell.concert.infra.db.concert.entity.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertDateJpaRepository extends JpaRepository<ConcertDateEntity, Long> {
    List<ConcertDateEntity> findAllByDeletedAtIsNull();
    List<ConcertDateEntity> findAllByConcertIdAndDeletedAtIsNull();
    List<ConcertDateEntity> findAllByConcertDateAndDeletedAtIsNull();
    List<ConcertDateEntity> findAllByConcertIdAndConcertDateAndDeletedAtIsNull();

}

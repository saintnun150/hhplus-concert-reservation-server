package org.lowell.concert.infra.db.concert.repository;

import org.lowell.concert.infra.db.concert.entity.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertJpaRepository extends JpaRepository<ConcertEntity, Long> {
    List<ConcertEntity> findAllByDeletedAtIsNull();
}

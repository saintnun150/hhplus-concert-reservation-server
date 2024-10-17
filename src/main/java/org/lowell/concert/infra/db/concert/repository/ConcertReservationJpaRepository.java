package org.lowell.concert.infra.db.concert.repository;

import org.lowell.concert.infra.db.concert.entity.ConcertReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservationEntity, Long> {
    List<ConcertReservationEntity> findAllByUserId(Long userId);
}

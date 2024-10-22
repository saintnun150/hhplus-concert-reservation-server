package org.lowell.concert.infra.db.concert.repository;

import org.lowell.concert.domain.concert.model.ConcertReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservation, Long> {
    List<ConcertReservation> findAllByUserId(Long userId);
}

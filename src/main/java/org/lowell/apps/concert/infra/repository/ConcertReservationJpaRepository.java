package org.lowell.apps.concert.infra.repository;

import jakarta.persistence.LockModeType;
import org.lowell.apps.concert.domain.model.ConcertReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConcertReservationJpaRepository extends JpaRepository<ConcertReservation, Long> {
    List<ConcertReservation> findAllByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("" +
            " select cr" +
            " from ConcertReservation cr" +
            " where cr.reservationId =:reservationId")
    Optional<ConcertReservation> findByIdWithLock(Long reservationId);
}

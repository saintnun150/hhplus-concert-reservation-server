package org.lowell.apps.concert.infra.repository;

import jakarta.persistence.LockModeType;
import org.lowell.apps.concert.domain.model.ConcertSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("" +
            " select cs" +
            " from ConcertSeat cs" +
            " where cs.seatId =:seatId")
    Optional<ConcertSeat> findByIdWithLock(@Param("seatId") Long seatId);

    List<ConcertSeat> findAllByConcertScheduleId(Long concertScheduleId);
}

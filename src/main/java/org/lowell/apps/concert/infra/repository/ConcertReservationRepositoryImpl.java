package org.lowell.apps.concert.infra.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.concert.domain.dto.ConcertReservationCommand;
import org.lowell.apps.concert.domain.dto.ConcertReservationQuery;
import org.lowell.apps.concert.domain.model.ConcertReservation;
import org.lowell.apps.concert.domain.model.ReservationStatus;
import org.lowell.apps.concert.domain.repository.ConcertReservationRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertReservationRepositoryImpl implements ConcertReservationRepository {
    private final ConcertReservationJpaRepository jpaRepository;

    @Override
    public ConcertReservation createConcertReservation(ConcertReservationCommand.Create command) {
        ConcertReservation entity = ConcertReservation.builder()
                                                      .seatId(command.seatId())
                                                      .userId(command.userId())
                                                      .status(ReservationStatus.PENDING)
                                                      .createdAt(LocalDateTime.now())
                                                      .build();
        return jpaRepository.save(entity);
    }

    @Override
    public Optional<ConcertReservation> getConcertReservation(ConcertReservationQuery.Search query) {
        return jpaRepository.findById(query.concertReservationId());
    }

    @Override
    public Optional<ConcertReservation> getConcertReservationWithLock(ConcertReservationQuery.Search query) {
        return jpaRepository.findByIdWithLock(query.concertReservationId());
    }

    @Override
    public List<ConcertReservation> getConcertReservations(ConcertReservationQuery.SearchList query) {
        return jpaRepository.findAllByUserId(query.userId());
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}

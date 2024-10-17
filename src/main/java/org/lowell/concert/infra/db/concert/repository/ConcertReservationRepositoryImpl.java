package org.lowell.concert.infra.db.concert.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.concert.ConcertReservationMapper;
import org.lowell.concert.domain.concert.dto.ConcertReservationCommand;
import org.lowell.concert.domain.concert.dto.ConcertReservationQuery;
import org.lowell.concert.domain.concert.model.ConcertReservationInfo;
import org.lowell.concert.domain.concert.model.ReservationStatus;
import org.lowell.concert.domain.concert.repository.ConcertReservationRepository;
import org.lowell.concert.infra.db.concert.entity.ConcertReservationEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertReservationRepositoryImpl implements ConcertReservationRepository {
    private final ConcertReservationJpaRepository jpaRepository;
    private final ConcertReservationMapper mapper;

    @Override
    public void createConcertReservation(ConcertReservationCommand.Create command) {
        ConcertReservationEntity entity = ConcertReservationEntity.builder()
                                                                  .seatId(command.seatId())
                                                                  .userId(command.userId())
                                                                  .status(ReservationStatus.RESERVED)
                                                                  .createdAt(LocalDateTime.now())
                                                                  .build();
        jpaRepository.save(entity);
    }

    @Override
    public ConcertReservationInfo getConcertReservation(ConcertReservationQuery.Search query) {
        ConcertReservationEntity entity = jpaRepository.findById(query.concertReservationId())
                                                       .orElse(null);
        return mapper.toPojo(entity);
    }

    @Override
    public List<ConcertReservationInfo> getConcertReservations(ConcertReservationQuery.SearchList query) {
        List<ConcertReservationEntity> entities = jpaRepository.findAllByUserId(query.userId());
        return mapper.toPojoList(entities);
    }

    @Transactional
    @Override
    public void updateConcertReservation(ConcertReservationCommand.Update command) {
        jpaRepository.findById(command.concertReservationId())
                     .ifPresent(entity -> entity.updateConcertReservationState(command.status()));
    }
}

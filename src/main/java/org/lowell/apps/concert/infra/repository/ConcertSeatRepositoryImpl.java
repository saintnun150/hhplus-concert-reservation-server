package org.lowell.apps.concert.infra.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.concert.domain.dto.ConcertSeatCommand;
import org.lowell.apps.concert.domain.dto.ConcertSeatQuery;
import org.lowell.apps.concert.domain.model.ConcertSeat;
import org.lowell.apps.concert.domain.repository.ConcertSeatRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertSeatRepositoryImpl implements ConcertSeatRepository {
    private final ConcertSeatJpaRepository jpaRepository;

    @Override
    public void createConcertSeat(ConcertSeatCommand.Create command) {
        ConcertSeat entity = ConcertSeat.builder()
                                        .concertScheduleId(command.scheduleId())
                                        .seatNo(command.seatNo())
                                        .status(command.status())
                                        .price(command.price())
                                        .build();
        jpaRepository.save(entity);

    }

    @Override
    public Optional<ConcertSeat> getConcertSeat(ConcertSeatQuery.Search query) {
        return jpaRepository.findById(query.seatId());
    }

    @Override
    public Optional<ConcertSeat> getConcertSeatWithLock(ConcertSeatQuery.Search query) {
        return jpaRepository.findByIdWithLock(query.seatId());
    }

    @Override
    public List<ConcertSeat> getConcertSeats(ConcertSeatQuery.SearchList query) {
        List<ConcertSeat> entities = jpaRepository.findAllByConcertScheduleId(query.concertScheduleId());
        return entities;
    }

    @Override
    public void saveAll(List<ConcertSeat> concertSeats) {
        jpaRepository.saveAll(concertSeats);
    }

    @Transactional
    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }


}

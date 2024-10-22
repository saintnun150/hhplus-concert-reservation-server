package org.lowell.concert.infra.db.concert.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.dto.ConcertSeatQuery;
import org.lowell.concert.domain.concert.model.ConcertSeat;
import org.lowell.concert.domain.concert.repository.ConcertSeatRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertSeatRepositoryImpl implements ConcertSeatRepository {
    private final ConcertSeatJpaRepository jpaRepository;

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
}

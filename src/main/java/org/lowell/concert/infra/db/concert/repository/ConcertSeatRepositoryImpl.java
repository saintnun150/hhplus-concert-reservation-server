package org.lowell.concert.infra.db.concert.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.concert.ConcertSeatMapper;
import org.lowell.concert.domain.concert.dto.ConcertSeatQuery;
import org.lowell.concert.domain.concert.model.ConcertSeatInfo;
import org.lowell.concert.domain.concert.repository.ConcertSeatRepository;
import org.lowell.concert.infra.db.concert.entity.ConcertSeatEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertSeatRepositoryImpl implements ConcertSeatRepository {
    private final ConcertSeatJpaRepository jpaRepository;
    private final ConcertSeatMapper mapper;

    @Override
    public ConcertSeatInfo getConcertSeat(ConcertSeatQuery.Search query) {
        ConcertSeatEntity entity = jpaRepository.findByConcertDateIdAndSeatNo(query.concertDateId(),
                                                                              query.seatNo());
        return mapper.toPojo(entity);
    }

    @Override
    public List<ConcertSeatInfo> getConcertSeats(ConcertSeatQuery.SearchList query) {
        List<ConcertSeatEntity> entities = jpaRepository.findAllByConcertDateId(query.concertDateId());
        return mapper.toPojoList(entities);
    }
}

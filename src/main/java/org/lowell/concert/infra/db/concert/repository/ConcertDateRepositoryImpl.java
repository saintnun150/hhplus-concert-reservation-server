package org.lowell.concert.infra.db.concert.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.concert.ConcertDateMapper;
import org.lowell.concert.domain.concert.dto.ConcertDateCommand;
import org.lowell.concert.domain.concert.model.ConcertDateInfo;
import org.lowell.concert.domain.concert.repository.ConcertDateRepository;
import org.lowell.concert.infra.db.concert.entity.ConcertDateEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertDateRepositoryImpl implements ConcertDateRepository {
    private final ConcertDateJpaRepository jpaRepository;
    private final ConcertDateMapper mapper;

    @Override
    @Transactional
    public void createConcertDate(ConcertDateCommand.Create command) {
        ConcertDateEntity entity = ConcertDateEntity.builder()
                                                    .concertId(command.concertId())
                                                    .concertDate(command.concertDate())
                                                    .beginTime(command.beginTime())
                                                    .endTime(command.endTime())
                                                    .createdAt(LocalDateTime.now())
                                                    .build();
        jpaRepository.save(entity);
    }

    @Override
    public ConcertDateInfo getConcertDate(long concertDateId) {
        ConcertDateEntity entity = jpaRepository.findById(concertDateId)
                                                .orElse(null);
        return mapper.toPojo(entity);
    }

    @Override
    public List<ConcertDateInfo> getConcertDates() {
        List<ConcertDateEntity> entities = jpaRepository.findAllByDeletedAtIsNull();
        return mapper.toPojoList(entities);
    }

    @Override
    public List<ConcertDateInfo> getConcertDates(long concertId) {
        List<ConcertDateEntity> entities = jpaRepository.findAllByConcertIdAndDeletedAtIsNull(concertId);
        return mapper.toPojoList(entities);
    }

    @Override
    public List<ConcertDateInfo> getConcertDates(LocalDateTime concertDate) {
        List<ConcertDateEntity> entities = jpaRepository.findAllByConcertDateAndDeletedAtIsNull(concertDate);
        return mapper.toPojoList(entities);
    }

    @Override
    public List<ConcertDateInfo> getConcertDates(long concertId, LocalDateTime concertDate) {
        List<ConcertDateEntity> entities = jpaRepository.findAllByConcertIdAndConcertDateAndDeletedAtIsNull(concertId, concertDate);
        return mapper.toPojoList(entities);
    }
}

package org.lowell.concert.infra.db.concert.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.concert.ConcertMapper;
import org.lowell.concert.domain.concert.dto.ConcertCommand;
import org.lowell.concert.domain.concert.model.ConcertInfo;
import org.lowell.concert.domain.concert.repository.ConcertRepository;
import org.lowell.concert.infra.db.concert.entity.ConcertEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {
    private final ConcertJpaRepository jpaRepository;
    private final ConcertMapper mapper;

    @Override
    public void createConcert(ConcertCommand.Create command) {
        ConcertEntity entity = ConcertEntity.builder()
                                            .name(command.name())
                                            .createdAt(LocalDateTime.now())
                                            .build();
        jpaRepository.save(entity);
    }

    @Override
    public ConcertInfo getConcert(long concertId) {
        ConcertEntity entity = jpaRepository.findById(concertId)
                                            .orElse(null);
        return mapper.toPojo(entity);
    }

    @Override
    public List<ConcertInfo> getConcerts() {
        List<ConcertEntity> entities = jpaRepository.findAllByDeletedAtIsNull();
        return mapper.toPojoList(entities);
    }
}

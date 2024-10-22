package org.lowell.concert.infra.db.concert.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.dto.ConcertCommand;
import org.lowell.concert.domain.concert.model.Concert;
import org.lowell.concert.domain.concert.repository.ConcertRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {
    private final ConcertJpaRepository jpaRepository;

    @Override
    public void createConcert(ConcertCommand.Create command) {
        Concert entity = Concert.builder()
                                .name(command.name())
                                .createdAt(LocalDateTime.now())
                                .build();
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Concert> getConcert(long concertId) {
        return jpaRepository.findById(concertId);
    }


    @Override
    public List<Concert> getConcerts() {
        return jpaRepository.findAllByDeletedAtIsNull();
    }
}

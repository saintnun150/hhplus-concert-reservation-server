package org.lowell.concert.infra.db.concert.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.dto.ConcertQuery;
import org.lowell.concert.domain.concert.model.Concert;
import org.lowell.concert.domain.concert.repository.ConcertRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {
    private final ConcertJpaRepository jpaRepository;

    public Optional<Concert> getConcert(long concertId) {
        return jpaRepository.findById(concertId);
    }

    @Override
    public List<Concert> getConcerts(ConcertQuery.SearchList query) {
        boolean hasName = StringUtils.hasText(query.name());
        boolean hasFromAndTo = query.from() != null && query.to() != null;

        if (hasName && hasFromAndTo) {
            return jpaRepository.findAllByNameContainingAndOpenedAtBetween(query.name(), query.from(), query.to());
        }
        if (hasName) {
            return jpaRepository.findAllByNameContaining(query.name());
        }
        if (hasFromAndTo) {
            return jpaRepository.findAllByOpenedAtBetween(query.from(), query.to());
        }
        return jpaRepository.findAll();
    }

}

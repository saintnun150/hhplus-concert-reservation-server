package org.lowell.concert.infra.db.concert.repository;

import org.lowell.concert.domain.concert.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
    List<Concert> findAllByDeletedAtIsNull();
}

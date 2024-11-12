package org.lowell.concert.infra.db.concert.repository;

import org.lowell.concert.domain.concert.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
    List<Concert> findAllByNameContaining(String name);
    List<Concert> findAllByNameContainingAndOpenedAtBetween(String name, LocalDateTime from, LocalDateTime to);
    List<Concert> findAllByOpenedAtBetween(LocalDateTime from, LocalDateTime to);

    @Query(value = "" +
            " SELECT *" +
            " FROM concert" +
            " WHERE MATCH(name)" +
            " AGAINST(:name IN BOOLEAN MODE)", nativeQuery = true)
    List<Concert> findAllByNameFullText(@Param("name") String concertName);
}

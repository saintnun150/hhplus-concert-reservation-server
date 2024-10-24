package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.dto.ConcertReservationCommand;
import org.lowell.concert.domain.concert.dto.ConcertReservationQuery;
import org.lowell.concert.domain.concert.exception.ConcertReservationError;
import org.lowell.concert.domain.concert.model.ConcertReservation;
import org.lowell.concert.domain.concert.repository.ConcertReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertReservationService {
    private final ConcertReservationRepository concertReservationRepository;

    @Transactional
    public ConcertReservation createConcertReservation(ConcertReservationCommand.Create command) {
        return concertReservationRepository.createConcertReservation(command);
    }

    public ConcertReservation getConcertReservation(ConcertReservationQuery.Search query) {
        return concertReservationRepository.getConcertReservation(query)
                                           .orElseThrow(() -> DomainException.create(ConcertReservationError.NOT_FOUND_RESERVATION, DomainException.createPayload(query)));
    }

    public List<ConcertReservation> getConcertReservations(ConcertReservationQuery.SearchList query) {
        List<ConcertReservation> reservations = concertReservationRepository.getConcertReservations(query);
        if (CollectionUtils.isEmpty(reservations)) {
            throw DomainException.create(ConcertReservationError.NOT_FOUND_RESERVATION, DomainException.createPayload(query));
        }
        return reservations;
    }

}

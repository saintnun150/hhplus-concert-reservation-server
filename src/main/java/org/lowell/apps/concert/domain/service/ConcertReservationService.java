package org.lowell.apps.concert.domain.service;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.concert.domain.dto.ConcertReservationCommand;
import org.lowell.apps.concert.domain.dto.ConcertReservationQuery;
import org.lowell.apps.concert.domain.exception.ConcertReservationError;
import org.lowell.apps.concert.domain.model.ConcertReservation;
import org.lowell.apps.concert.domain.repository.ConcertReservationRepository;
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

    public ConcertReservation getConcertReservationWithLock(ConcertReservationQuery.Search query) {
        return concertReservationRepository.getConcertReservationWithLock(query)
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

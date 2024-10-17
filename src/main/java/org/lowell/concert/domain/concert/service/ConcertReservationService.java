package org.lowell.concert.domain.concert.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.concert.dto.ConcertReservationCommand;
import org.lowell.concert.domain.concert.dto.ConcertReservationQuery;
import org.lowell.concert.domain.concert.exception.ConcertReservationErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertReservationException;
import org.lowell.concert.domain.concert.model.ConcertReservationInfo;
import org.lowell.concert.domain.concert.repository.ConcertReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertReservationService {
    private final ConcertReservationRepository concertReservationRepository;

    @Transactional
    public void createConcertReservation(ConcertReservationCommand.Create command) {
        concertReservationRepository.createConcertReservation(command);
    }

    public ConcertReservationInfo getConcertReservation(ConcertReservationQuery.Search query) {
        ConcertReservationInfo reservation = concertReservationRepository.getConcertReservation(query);
        if (reservation == null) {
            throw new ConcertReservationException(ConcertReservationErrorCode.NOT_FOUND_RESERVATION);
        }
        return reservation;
    }

    public List<ConcertReservationInfo> getConcertReservations(ConcertReservationQuery.SearchList query) {
        List<ConcertReservationInfo> reservations = concertReservationRepository.getConcertReservations(query);
        if (reservations == null) {
            throw new ConcertReservationException(ConcertReservationErrorCode.NOT_FOUND_RESERVATION);
        }
        return reservations;
    }

    @Transactional
    public void updateConcertReservation(ConcertReservationCommand.Update command) {
        concertReservationRepository.updateConcertReservation(command);
    }
}

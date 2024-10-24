package org.lowell.concert.domain.concert.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.dto.ConcertReservationQuery;
import org.lowell.concert.domain.concert.exception.ConcertReservationError;
import org.lowell.concert.domain.concert.repository.ConcertReservationRepository;
import org.lowell.concert.domain.concert.service.ConcertReservationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertReservationServiceTest {

    @Mock
    private ConcertReservationRepository reservationRepository;

    @InjectMocks
    private ConcertReservationService reservationService;


    @DisplayName("조회된 예약이 없을 경우 예외를 반환한다.")
    @Test
    void testGetConcertReservation_NotFound() {
        Long reservationId = 1L;
        ConcertReservationQuery.Search query = new ConcertReservationQuery.Search(reservationId);
        when(reservationRepository.getConcertReservation(query)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.getConcertReservation(query))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertReservationError.NOT_FOUND_RESERVATION);
                });
    }

    @DisplayName("사용자 Id로 조회된 예약이 없을 경우 예외를 반환한다.")
    @Test
    void testGetConcertReservations_NotFound() {
        Long userId = 1L;
        ConcertReservationQuery.SearchList query = new ConcertReservationQuery.SearchList(userId);
        when(reservationRepository.getConcertReservations(query)).thenReturn(null);

        assertThatThrownBy(() -> reservationService.getConcertReservations(query))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertReservationError.NOT_FOUND_RESERVATION);
                });
    }

}
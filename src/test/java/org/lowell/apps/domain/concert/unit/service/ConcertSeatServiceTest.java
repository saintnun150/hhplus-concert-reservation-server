package org.lowell.apps.domain.concert.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.concert.domain.dto.ConcertSeatQuery;
import org.lowell.apps.concert.domain.exception.ConcertSeatError;
import org.lowell.apps.concert.domain.model.ConcertSeat;
import org.lowell.apps.concert.domain.model.SeatStatus;
import org.lowell.apps.concert.domain.repository.ConcertSeatRepository;
import org.lowell.apps.concert.domain.service.ConcertSeatService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertSeatServiceTest {

    @Mock
    private ConcertSeatRepository concertSeatRepository;

    @InjectMocks
    private ConcertSeatService concertSeatService;


    @DisplayName("콘서트 날짜로 좌석 조회결과 없다면 예외를 반환한다.")
    @Test
    void throwException_when_empty_concert_seats() {
        long concertDateId = 1L;
        LocalDateTime now = LocalDateTime.now();
        ConcertSeatQuery.SearchList query = new ConcertSeatQuery.SearchList(concertDateId, now);
        when(concertSeatRepository.getConcertSeats(query)).thenReturn(null);

        assertThatThrownBy(() -> concertSeatService.getConcertSeats(query))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertSeatError.NOT_FOUND_SEAT);
                });
    }

    @DisplayName("콘서트 날짜와 좌석번호로 조회한 좌석이 없을 경우 예외를 반환한다.")
    @Test
    void throwException_when_search_concert_seats_not_found() {
        long concertDateId = 1L;
        LocalDateTime now = LocalDateTime.now();
        ConcertSeatQuery.SearchList query = new ConcertSeatQuery.SearchList(concertDateId, now);
        when(concertSeatRepository.getConcertSeats(query)).thenReturn(null);

        assertThatThrownBy(() -> concertSeatService.getConcertSeats(query))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertSeatError.NOT_FOUND_SEAT);
                });
    }

    @DisplayName("좌석이 비어있는지 확인한다.")
    @Test
    void checkEmptySeat() {
        long concertDateId = 1L;
        LocalDateTime now = LocalDateTime.now();
        ConcertSeat concertSeat = ConcertSeat.builder()
                                             .seatId(1L)
                                             .concertScheduleId(concertDateId)
                                             .seatNo(1)
                                             .status(SeatStatus.EMPTY)
                                             .price(10000)
                                             .tempReservedAt(null)
                                             .reservedAt(null)
                                             .build();

        ConcertSeat concertSeat2 = ConcertSeat.builder()
                                              .seatId(1L)
                                              .concertScheduleId(concertDateId)
                                              .seatNo(2)
                                              .status(SeatStatus.EMPTY)
                                              .price(10000)
                                              .tempReservedAt(null)
                                              .reservedAt(null)
                                              .build();

        ConcertSeat concertSeat3 = ConcertSeat.builder()
                                              .seatId(1L)
                                              .concertScheduleId(concertDateId)
                                              .seatNo(3)
                                              .status(SeatStatus.OCCUPIED)
                                              .price(10000)
                                              .tempReservedAt(null)
                                              .reservedAt(null)
                                              .build();

        List<ConcertSeat> seats = List.of(concertSeat, concertSeat2, concertSeat3);


        when(concertSeatRepository.getConcertSeats(new ConcertSeatQuery.SearchList(concertDateId, now)))
                .thenReturn(seats);

        List<ConcertSeat> availableSeats = concertSeatService.getAvailableSeats(new ConcertSeatQuery.SearchList(concertDateId, now));

        assertThat(availableSeats).hasSize(2);
        assertThat(availableSeats).contains(concertSeat, concertSeat2);
    }


    @DisplayName("비어있는 좌석이 없으면 예외가 발생한다.")
    @Test
    void throwException_when_empty_available_seats() {
        long concertDateId = 1L;
        LocalDateTime now = LocalDateTime.now();
        ConcertSeat concertSeat = ConcertSeat.builder()
                                             .seatId(1L)
                                             .concertScheduleId(concertDateId)
                                             .seatNo(1)
                                             .status(SeatStatus.OCCUPIED)
                                             .price(10000)
                                             .tempReservedAt(null)
                                             .reservedAt(null)
                                             .build();

        ConcertSeat concertSeat2 = ConcertSeat.builder()
                                              .seatId(1L)
                                              .concertScheduleId(concertDateId)
                                              .seatNo(2)
                                              .status(SeatStatus.OCCUPIED)
                                              .price(10000)
                                              .tempReservedAt(null)
                                              .reservedAt(null)
                                              .build();

        ConcertSeat concertSeat3 = ConcertSeat.builder()
                                              .seatId(1L)
                                              .concertScheduleId(concertDateId)
                                              .seatNo(3)
                                              .status(SeatStatus.OCCUPIED)
                                              .price(10000)
                                              .tempReservedAt(null)
                                              .reservedAt(null)
                                              .build();

        List<ConcertSeat> seats = List.of(concertSeat, concertSeat2, concertSeat3);

        when(concertSeatRepository.getConcertSeats(new ConcertSeatQuery.SearchList(concertDateId,now)))
                .thenReturn(seats);

        assertThatThrownBy(() -> concertSeatService.getAvailableSeats(new ConcertSeatQuery.SearchList(concertDateId, now)))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertSeatError.NOT_FOUND_AVAILABLE_SEAT);
                });


    }

}
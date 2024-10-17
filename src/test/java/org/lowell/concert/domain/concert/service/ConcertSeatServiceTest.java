package org.lowell.concert.domain.concert.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.concert.dto.ConcertSeatQuery;
import org.lowell.concert.domain.concert.exception.ConcertSeatErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertSeatException;
import org.lowell.concert.domain.concert.repository.ConcertSeatRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        ConcertSeatQuery.SearchList query = new ConcertSeatQuery.SearchList(concertDateId);
        when(concertSeatRepository.getConcertSeats(query)).thenReturn(null);

        assertThatThrownBy(() -> concertSeatService.getConcertSeats(query))
                .isInstanceOfSatisfying(ConcertSeatException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertSeatErrorCode.NOT_FOUND_SEAT);
                });
    }

    @DisplayName("콘서트 날짜와 좌석번호로 조회한 좌석이 없을 경우 예외를 반환한다.")
    @Test
    void throwException_when_search_concert_seats_not_found() {
        long concertDateId = 1L;
        int seatNo = 1;
        ConcertSeatQuery.Search query = new ConcertSeatQuery.Search(concertDateId, seatNo);
        when(concertSeatRepository.getConcertSeat(query)).thenReturn(null);

        assertThatThrownBy(() -> concertSeatService.getConcertSeat(query))
                .isInstanceOfSatisfying(ConcertSeatException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertSeatErrorCode.NOT_FOUND_SEAT);
                });
    }

}
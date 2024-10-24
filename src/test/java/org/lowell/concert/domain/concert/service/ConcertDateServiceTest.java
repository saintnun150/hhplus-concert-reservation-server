package org.lowell.concert.domain.concert.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.concert.dto.ConcertDateCommand;
import org.lowell.concert.domain.concert.dto.ConcertDateQuery;
import org.lowell.concert.domain.concert.exception.ConcertDateErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertDateException;
import org.lowell.concert.domain.concert.repository.ConcertDateRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertDateServiceTest {

    @Mock
    private ConcertDateRepository concertDateRepository;

    @InjectMocks
    private ConcertDateService concertDateService;

    @DisplayName("콘서트 날짜 생성시 콘서트 ID나 콘서트일이 없으면 예외가 발생한다.")
    @Test
    void throwException_when_invalid_create_params() {
        ConcertDateCommand.Create command = new ConcertDateCommand.Create(null, LocalDateTime.now(), null, null);
        assertThatThrownBy(() -> concertDateService.createConcertDate(command))
                .isInstanceOfSatisfying(ConcertDateException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertDateErrorCode.INVALID_CONCERT_ID);
                });

        ConcertDateCommand.Create command2 = new ConcertDateCommand.Create(1L, null, null, null);
        assertThatThrownBy(() -> concertDateService.createConcertDate(command2))
                .isInstanceOfSatisfying(ConcertDateException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertDateErrorCode.INVALID_CONCERT_DATE);
                });
    }

    @DisplayName("콘서트 날짜 조회결과가 없으면 예외가 발생한다.")
    @Test
    void throwException_when_empty_concert_date() {
        ConcertDateQuery.SearchList query = new ConcertDateQuery.SearchList(1L, LocalDateTime.now());
        when(concertDateRepository.getConcertDates(1L, LocalDateTime.now())).thenReturn(null);
        assertThatThrownBy(() -> concertDateService.getConcertDates(query))
                .isInstanceOfSatisfying(ConcertDateException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertDateErrorCode.NOT_FOUND_CONCERT_DATE);
                });
    }

}
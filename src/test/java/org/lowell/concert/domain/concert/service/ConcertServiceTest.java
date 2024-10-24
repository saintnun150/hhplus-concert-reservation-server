package org.lowell.concert.domain.concert.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.concert.dto.ConcertCommand;
import org.lowell.concert.domain.concert.exception.ConcertErrorCode;
import org.lowell.concert.domain.concert.exception.ConcertException;
import org.lowell.concert.domain.concert.repository.ConcertRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @DisplayName("콘서트 생성 시 이름이 없으면 예외가 발생한다.")
    @Test
    void throwException_when_empty_concertName() {
        String concertName = "";
        ConcertCommand.Create command = new ConcertCommand.Create(concertName);
        assertThatThrownBy(() -> concertService.createConcert(command))
                .isInstanceOfSatisfying(ConcertException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertErrorCode.INVALID_CONCERT_NAME);
                });
    }

    @DisplayName("콘서트 Id로 조회시 없을 경우 예외가 발생한다.")
    @Test
    void throwException_when_search_invalid_concertId() {
        long concertId = 1L;
        when(concertRepository.getConcert(concertId)).thenReturn(null);
        assertThatThrownBy(() -> concertService.getConcert(concertId))
                .isInstanceOfSatisfying(ConcertException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertErrorCode.NOT_FOUND_CONCERT);
                });
    }

    @DisplayName("콘서트 목록 조회시 없을 경우 예외가 발생한다.")
    @Test
    void throwException_when_search_concert() {
        when(concertRepository.getConcerts()).thenReturn(null);
        assertThatThrownBy(() -> concertService.getConcerts())
                .isInstanceOfSatisfying(ConcertException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(ConcertErrorCode.NOT_FOUND_CONCERT);
                });
    }

}
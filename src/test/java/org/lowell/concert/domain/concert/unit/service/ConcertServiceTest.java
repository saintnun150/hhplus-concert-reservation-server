package org.lowell.concert.domain.concert.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.exception.ConcertError;
import org.lowell.concert.domain.concert.repository.ConcertRepository;
import org.lowell.concert.domain.concert.service.ConcertService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @DisplayName("콘서트 Id로 조회시 없을 경우 예외가 발생한다.")
    @Test
    void throwException_when_search_invalid_concertId() {
        long concertId = 1L;
        when(concertRepository.getConcert(concertId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> concertService.getConcert(concertId))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertError.NOT_FOUND_CONCERT);
                });
    }

}
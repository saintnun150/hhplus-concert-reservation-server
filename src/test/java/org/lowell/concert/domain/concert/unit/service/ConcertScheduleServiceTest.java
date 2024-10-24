package org.lowell.concert.domain.concert.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.concert.dto.ConcertScheduleCommand;
import org.lowell.concert.domain.concert.dto.ConcertScheduleQuery;
import org.lowell.concert.domain.concert.exception.ConcertScheduleError;
import org.lowell.concert.domain.concert.repository.ConcertScheduleRepository;
import org.lowell.concert.domain.concert.service.ConcertScheduleService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertScheduleServiceTest {

    @Mock
    private ConcertScheduleRepository concertScheduleRepository;

    @InjectMocks
    private ConcertScheduleService concertScheduleService;

    @DisplayName("콘서트 날짜 생성시 콘서트 ID나 콘서트일이 없으면 예외가 발생한다.")
    @Test
    void throwException_when_invalid_create_params() {
        ConcertScheduleCommand.Create command = new ConcertScheduleCommand.Create(null, LocalDateTime.now(), null, null);
        assertThatThrownBy(() -> concertScheduleService.createConcertSchedule(command))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertScheduleError.INVALID_CONCERT_ID);
                });

        ConcertScheduleCommand.Create command2 = new ConcertScheduleCommand.Create(1L, null, null, null);
        assertThatThrownBy(() -> concertScheduleService.createConcertSchedule(command2))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertScheduleError.INVALID_SCHEDULE_DATE);
                });
    }

    @DisplayName("콘서트 날짜 조회결과가 없으면 예외가 발생한다.")
    @Test
    void throwException_when_empty_concert_date() {
        ConcertScheduleQuery.SearchList query = new ConcertScheduleQuery.SearchList(1L, LocalDateTime.now());
        when(concertScheduleRepository.getConcertDates(1L, LocalDateTime.now())).thenReturn(null);
        assertThatThrownBy(() -> concertScheduleService.getConcertSchedules(query))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(ConcertScheduleError.NOT_FOUND_CONCERT_SCHEDULE);
                });
    }

}
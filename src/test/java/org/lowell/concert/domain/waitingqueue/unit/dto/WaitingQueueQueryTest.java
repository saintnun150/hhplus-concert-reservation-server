package org.lowell.concert.domain.waitingqueue.unit.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.lowell.concert.domain.waitingqueue.dto.WaitingQueueQuery;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueException;

import static org.junit.jupiter.api.Assertions.*;

class WaitingQueueQueryTest {

    @DisplayName("대기열 조회 시 token이 없으면 예외가 발생한다.")
    @Test
    void throwExceptionWhenTokenIsEmpty() {
        // given
        String token = "";

        // then
        assertThrows(WaitingQueueException.class, () -> {
            new WaitingQueueQuery.GetQueue(token);
        });
    }


}
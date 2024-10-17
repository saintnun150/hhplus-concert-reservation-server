package org.lowell.concert.domain.concert.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainErrorResponse;

@Getter
@RequiredArgsConstructor
public enum ConcertReservationErrorCode implements DomainErrorCode {
    STATE_COMPLETE(404, "CONCERT_RESERVATION_ERR_01", "reservation state is already completed"),
    NOT_FOUND_RESERVATION(404, "CONCERT_RESERVATION_ERR_02", "not fount reservation"),
    ;

    private final int status;
    private final String code;
    private final String message;

    @Override
    public DomainErrorResponse getErrorResponse() {
        return DomainErrorCode.super.getErrorResponse();
    }
}

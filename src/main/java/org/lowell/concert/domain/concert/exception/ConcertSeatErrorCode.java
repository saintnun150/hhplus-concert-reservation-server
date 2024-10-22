package org.lowell.concert.domain.concert.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainErrorResponse;

@Getter
@RequiredArgsConstructor
public enum ConcertSeatErrorCode implements DomainErrorCode {
    RESERVED_COMPLETE(404, "CONCERT_SEAT_ERR_01", "ConcertSeat is reserved complete."),
    RESERVED_TEMPORARY(404, "CONCERT_SEAT_ERR_02", "ConcertSeat is reserved temporary."),
    NOT_FOUND_SEAT(404, "CONCERT_DATE_ERR_03", "not fount seat"),
    NOT_FOUND_AVAILABLE_SEAT(404, "CONCERT_DATE_ERR_04", "not fount available seat"),
    ;

    private final int status;
    private final String code;
    private final String message;

    @Override
    public DomainErrorResponse getErrorResponse() {
        return DomainErrorCode.super.getErrorResponse();
    }
}

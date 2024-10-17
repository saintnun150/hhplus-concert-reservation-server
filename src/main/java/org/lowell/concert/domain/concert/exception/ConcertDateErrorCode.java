package org.lowell.concert.domain.concert.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainErrorResponse;

@Getter
@RequiredArgsConstructor
public enum ConcertDateErrorCode implements DomainErrorCode {
    NOT_FOUND_CONCERT_DATE(404, "CONCERT_DATE_ERR_01", "ConcertDate Not Found"),
    INVALID_CONCERT_DATE(404, "CONCERT_DATE_ERR_02", "ConcertDate cannot be empty"),
    INVALID_CONCERT_ID(404, "CONCERT_DATE_ERR_03", "ConcertId cannot be empty")
    ;

    private final int status;
    private final String code;
    private final String message;

    @Override
    public DomainErrorResponse getErrorResponse() {
        return DomainErrorCode.super.getErrorResponse();
    }
}

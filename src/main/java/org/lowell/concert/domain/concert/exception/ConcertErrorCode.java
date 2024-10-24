package org.lowell.concert.domain.concert.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainErrorResponse;

@Getter
@RequiredArgsConstructor
public enum ConcertErrorCode implements DomainErrorCode {
    NOT_FOUND_CONCERT(404, "CONCERT_ERR_01", "Concert Not Found"),
    INVALID_CONCERT_NAME(400, "CONCERT_ERR_02", "Concert name must be necessary"),
    ;

    private final int status;
    private final String code;
    private final String message;

    @Override
    public DomainErrorResponse getErrorResponse() {
        return DomainErrorCode.super.getErrorResponse();
    }
}

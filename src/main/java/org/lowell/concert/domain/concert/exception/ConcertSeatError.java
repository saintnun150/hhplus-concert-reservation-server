package org.lowell.concert.domain.concert.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainError;
import org.lowell.concert.domain.common.exception.ErrorCode;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum ConcertSeatError implements DomainError {
    RESERVED_COMPLETE(ErrorCode.VALIDATION, "ConcertSeat is reserved complete.", LogLevel.WARN),
    RESERVED_TEMPORARY(ErrorCode.VALIDATION, "ConcertSeat is reserved temporary.", LogLevel.WARN),
    RESERVED_EXPIRED(ErrorCode.VALIDATION, "ConcertSeat is reserved expired.", LogLevel.WARN),
    NOT_FOUND_SEAT(ErrorCode.NOT_FOUND, "not fount seat", LogLevel.WARN),
    NOT_FOUND_AVAILABLE_SEAT(ErrorCode.NOT_FOUND,"not fount available seat", LogLevel.WARN),
    ;

    ConcertSeatError(ErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    ConcertSeatError(ErrorCode code, LogLevel logLevel) {
        this.code = code;
        this.message = null;
        this.logLevel = logLevel;
    }

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    @Override
    public String getMessage() {
        return (this.message != null) ? this.message : this.code.getDefaultMessage();
    }

}

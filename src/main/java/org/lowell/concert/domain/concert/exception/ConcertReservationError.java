package org.lowell.concert.domain.concert.exception;

import lombok.Getter;
import org.lowell.concert.domain.common.exception.DomainError;
import org.lowell.concert.domain.common.exception.ErrorCode;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum ConcertReservationError implements DomainError {
    STATE_COMPLETE(ErrorCode.VALIDATION,  "reservation state is already completed", LogLevel.WARN),
    STATE_EXPIRED(ErrorCode.VALIDATION, "reservation state is already expired", LogLevel.WARN),
    NOT_FOUND_RESERVATION(ErrorCode.NOT_FOUND,  "not fount reservation", LogLevel.WARN),
    ;

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    ConcertReservationError(ErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    ConcertReservationError(ErrorCode code, LogLevel logLevel) {
        this.code = code;
        this.message = null;
        this.logLevel = logLevel;
    }

    @Override
    public String getMessage() {
        return (this.message != null) ? this.message : this.code.getDefaultMessage();
    }
}

package org.lowell.concert.domain.concert.exception;

import lombok.Getter;
import org.lowell.concert.domain.common.exception.DomainError;
import org.lowell.concert.domain.common.exception.ErrorCode;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum ConcertScheduleError implements DomainError {
    NOT_FOUND_CONCERT_SCHEDULE(ErrorCode.NOT_FOUND, "ConcertDate Not Found", LogLevel.WARN),
    INVALID_SCHEDULE_DATE(ErrorCode.VALIDATION,  "ConcertDate cannot be empty", LogLevel.WARN),
    INVALID_CONCERT_ID(ErrorCode.VALIDATION, "ConcertId cannot be empty", LogLevel.WARN),
    ;

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    ConcertScheduleError(ErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    ConcertScheduleError(ErrorCode code, LogLevel logLevel) {
        this.code = code;
        this.message = null;
        this.logLevel = logLevel;
    }

    @Override
    public String getMessage() {
        return (this.message != null) ? this.message : this.code.getDefaultMessage();
    }
}

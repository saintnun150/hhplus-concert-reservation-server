package org.lowell.apps.concert.domain.exception;

import lombok.Getter;
import org.lowell.apps.common.exception.DomainError;
import org.lowell.apps.common.exception.ErrorCode;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum ConcertError implements DomainError {
    NOT_FOUND_CONCERT(ErrorCode.NOT_FOUND, "Concert Not Found", LogLevel.WARN),
    INVALID_CONCERT_NAME(ErrorCode.BAD_REQUEST, "Concert name must be necessary", LogLevel.WARN),
    ;

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    ConcertError(ErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }


}

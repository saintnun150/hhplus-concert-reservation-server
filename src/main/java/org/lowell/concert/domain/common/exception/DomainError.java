package org.lowell.concert.domain.common.exception;

import org.springframework.boot.logging.LogLevel;

public interface DomainError {
    ErrorCode getCode();
    String getMessage();
    LogLevel getLogLevel();
}

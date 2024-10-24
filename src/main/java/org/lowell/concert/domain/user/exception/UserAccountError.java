package org.lowell.concert.domain.user.exception;

import lombok.Getter;
import org.lowell.concert.domain.common.exception.DomainError;
import org.lowell.concert.domain.common.exception.ErrorCode;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum UserAccountError implements DomainError {
    NOT_FOUND_ACCOUNT(ErrorCode.NOT_FOUND, "User account is Not Found", LogLevel.WARN),
    INVALID_AMOUNT(ErrorCode.BAD_REQUEST, "User account invalid charge or use money", LogLevel.WARN),
    EXCEED_BALANCE(ErrorCode.BAD_REQUEST, "user account balance is exceed alert", LogLevel.WARN);

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    UserAccountError(ErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    UserAccountError(ErrorCode code, LogLevel logLevel) {
        this.code = code;
        this.message = null;
        this.logLevel = logLevel;
    }

    @Override
    public String getMessage() {
        return (this.message != null) ? this.message : this.code.getDefaultMessage();
    }
}
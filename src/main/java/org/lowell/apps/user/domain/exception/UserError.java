package org.lowell.apps.user.domain.exception;

import lombok.Getter;
import org.lowell.apps.common.exception.DomainError;
import org.lowell.apps.common.exception.ErrorCode;
import org.springframework.boot.logging.LogLevel;



@Getter
public enum UserError implements DomainError {
    NOT_FOUND_USER(ErrorCode.NOT_FOUND, "User Not Found", LogLevel.WARN);

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    UserError(ErrorCode code, LogLevel logLevel) {
        this.code = code;
        this.message = null;
        this.logLevel = logLevel;
    }

    UserError(ErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    @Override
    public String getMessage() {
        return (this.message != null) ? this.message : this.code.getDefaultMessage();
    }
}

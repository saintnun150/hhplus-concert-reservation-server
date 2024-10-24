package org.lowell.concert.domain.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainError;
import org.lowell.concert.domain.common.exception.ErrorCode;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum PaymentError implements DomainError {
    NOT_FOUND_PAYMENT(ErrorCode.NOT_FOUND, "payment not found", LogLevel.WARN);

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    PaymentError(ErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    PaymentError(ErrorCode code, LogLevel logLevel) {
        this.code = code;
        this.message = null;
        this.logLevel = logLevel;
    }

    @Override
    public String getMessage() {
        return (this.message != null) ? this.message : this.code.getDefaultMessage();
    }
}

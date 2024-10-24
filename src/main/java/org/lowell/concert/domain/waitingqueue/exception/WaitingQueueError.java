package org.lowell.concert.domain.waitingqueue.exception;

import lombok.Getter;
import org.lowell.concert.domain.common.exception.DomainError;
import org.lowell.concert.domain.common.exception.ErrorCode;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum WaitingQueueError implements DomainError {
    NOT_FOUND_TOKEN(ErrorCode.NOT_FOUND, "Token Not Found", LogLevel.WARN),
    INVALID_TOKEN_INPUT(ErrorCode.BAD_REQUEST,  "Token must exist", LogLevel.DEBUG),
    INVALID_TOKEN_STATUS_INPUT(ErrorCode.BAD_REQUEST,  "Token status must exist or valid value", LogLevel.DEBUG),
    NOT_WAITING_STATUS(ErrorCode.VALIDATION,  "Token status is not waiting status", LogLevel.WARN),
    INVALID_WAITING_ORDER(ErrorCode.BAD_REQUEST,  "Invalid waiting order", LogLevel.WARN),
    NOT_ACTIVATE_STATUS(ErrorCode.VALIDATION,  "Token status is not activate status", LogLevel.WARN),
    TOKEN_EXPIRED(ErrorCode.VALIDATION,  "Token is expired", LogLevel.WARN),
    ILLEGAL_TOKEN_STATUS(ErrorCode.VALIDATION,  "Token status is illegal value", LogLevel.WARN),
    EMPTY_TOKENS(ErrorCode.BAD_REQUEST,  "Tokens is empty", LogLevel.DEBUG),
    EMPTY_TOKEN_IDS(ErrorCode.BAD_REQUEST,  "Token IDs is empty", LogLevel.DEBUG),
    INVALID_TOKEN_EXPIRES_DATE(ErrorCode.VALIDATION,  "Token expires date is unspecified value", LogLevel.WARN),
    EMPTY_TOKEN_ID(ErrorCode.BAD_REQUEST,  "Token id is not exist", LogLevel.DEBUG),;

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    WaitingQueueError(ErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }
}

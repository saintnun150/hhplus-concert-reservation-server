package org.lowell.apps.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.lowell.apps.common.exception.DomainError;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.common.exception.ErrorCode;
import org.lowell.apps.common.api.ApiErrorResponse;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomainException(DomainException e) {
        DomainError error = e.getDomainError();
        Map<String, Object> payload = e.getPayload();
        String logString = String.format("## ErrorCode[%s], Message[%s], Payload[%s]", error, error.getMessage(), payload);

        LogLevel logLevel = error.getLogLevel();
        switch (logLevel) {
            case ERROR -> log.error(logString, e);
            case WARN -> log.warn(logString);
            case INFO -> log.info(logString);
        }
        ApiErrorResponse response = ApiErrorResponse.of(error,
                                                        error.getMessage(),
                                                        e.getPayload());
        HttpStatusCode statusCode = mapToHttpStatus(error.getCode());

        return ResponseEntity.status(statusCode)
                             .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception e) {
        ErrorCode code = ErrorCode.INTERNAL_SERVER;
        ApiErrorResponse response = ApiErrorResponse.of(code,
                                                        e.getLocalizedMessage(),
                                                        Collections.emptyMap());
        return ResponseEntity.status(mapToHttpStatus(code))
                             .body(response);
    }


    private HttpStatusCode mapToHttpStatus(ErrorCode code) {
        return switch (code) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case BAD_REQUEST, VALIDATION -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}

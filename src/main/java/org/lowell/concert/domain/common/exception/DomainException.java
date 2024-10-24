package org.lowell.concert.domain.common.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    private final DomainErrorCode errorCode;

    public DomainException(DomainErrorCode errorCode) {
        super(errorCode.getErrorResponse().message());
        this.errorCode = errorCode;
    }
}

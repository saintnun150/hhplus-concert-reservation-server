package org.lowell.concert.domain.common.exception;

public interface DomainErrorCode {
    int getStatus();
    String getCode();
    String getMessage();

    default DomainErrorResponse getErrorResponse() {
        return new DomainErrorResponse(getStatus(), getCode(), getMessage());
    }
}

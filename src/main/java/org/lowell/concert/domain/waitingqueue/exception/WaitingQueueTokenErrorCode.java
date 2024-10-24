package org.lowell.concert.domain.waitingqueue.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainErrorResponse;

@Getter
@RequiredArgsConstructor
public enum WaitingQueueTokenErrorCode implements DomainErrorCode {
    NOT_FOUND_TOKEN(404, "TOKEN_ERR_01", "Token Not Found"),
    INVALID_TOKEN_STR(400, "TOKEN_ERR_02", "Token must exist"),
    INVALID_TOKEN_STATUS(400, "TOKEN_ERR_03", "Token status is unspecified value"),
    ILLEGAL_TOKEN_STATUS(400, "TOKEN_ERR_04", "Token status is illegal value"),
    EMPTY_TOKENS(400, "TOKEN_ERR_05" , "Tokens is empty"),
    EMPTY_TOKEN_IDS(400, "TOKEN_ERR_06" , "Token IDs is empty"),
    INVALID_TOKEN_EXPIRES_DATE(400, "TOKEN_ERR_07", "Token expires date is unspecified value"),;

    private final int status;
    private final String code;
    private final String message;

    @Override
    public DomainErrorResponse getErrorResponse() {
        return DomainErrorCode.super.getErrorResponse();
    }
}

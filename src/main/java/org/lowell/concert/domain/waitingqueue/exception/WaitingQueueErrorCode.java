package org.lowell.concert.domain.waitingqueue.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainErrorResponse;

@Getter
@RequiredArgsConstructor
public enum WaitingQueueErrorCode implements DomainErrorCode {
    NOT_FOUND_TOKEN(404, "TOKEN_ERR_01", "Token Not Found"),
    INVALID_TOKEN_INPUT(400, "TOKEN_ERR_02", "Token must exist"),
    INVALID_TOKEN_STATUS_INPUT(400, "TOKEN_ERR_03", "Token status must exist or valid value"),
    NOT_WAITING_STATUS(400, "TOKEN_ERR_04", "Token status is not waiting status"),
    INVALID_WAITING_ORDER(400, "TOKEN_ERR_05", "Invalid waiting order"),
    NOT_ACTIVATE_STATUS(400, "TOKEN_ERR_04", "Token status is not activate status"),
    TOKEN_EXPIRED(400, "TOKEN_ERR_05", "Token is expired"),
    ILLEGAL_TOKEN_STATUS(400, "TOKEN_ERR_04", "Token status is illegal value"),
    EMPTY_TOKENS(400, "TOKEN_ERR_05" , "Tokens is empty"),
    EMPTY_TOKEN_IDS(400, "TOKEN_ERR_06" , "Token IDs is empty"),
    INVALID_TOKEN_EXPIRES_DATE(400, "TOKEN_ERR_07", "Token expires date is unspecified value"),
    EMPTY_TOKEN_ID(400, "TOKEN_ERR_07", "Token id is not exist");

    private final int status;
    private final String code;
    private final String message;

    @Override
    public DomainErrorResponse getErrorResponse() {
        return DomainErrorCode.super.getErrorResponse();
    }
}

package org.lowell.concert.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainErrorResponse;

@Getter
@RequiredArgsConstructor
public enum UserAccountErrorCode implements DomainErrorCode {
    NOT_FOUND_ACCOUNT(404, "USER_ACCOUNT_ERR_01", "User account is Not Found"),
    INVALID_AMOUNT(400, "USER_ACCOUNT_ERR_02", "User account invalid charge money"),
    EXCEED_BALANCE(500, "USER_ACCOUNT_ERR_03", "user account balance is exceed alert");

    private final int status;
    private final String code;
    private final String message;

    @Override
    public DomainErrorResponse getErrorResponse() {
        return DomainErrorCode.super.getErrorResponse();
    }
}

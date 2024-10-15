package org.lowell.concert.domain.user.exception;

import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainException;

public class UserException extends DomainException {

    public UserException(DomainErrorCode errorCode) {
        super(errorCode);
    }
}

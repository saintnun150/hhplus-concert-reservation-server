package org.lowell.concert.domain.concert.exception;

import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainException;

public class ConcertSeatException extends DomainException {
    public ConcertSeatException(DomainErrorCode errorCode) {
        super(errorCode);
    }
}

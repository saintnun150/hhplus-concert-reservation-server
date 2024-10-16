package org.lowell.concert.domain.concert.exception;

import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainException;

public class ConcertException extends DomainException {
    public ConcertException(DomainErrorCode errorCode) {
        super(errorCode);
    }
}

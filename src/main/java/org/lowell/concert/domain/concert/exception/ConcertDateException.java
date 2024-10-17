package org.lowell.concert.domain.concert.exception;

import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainException;

public class ConcertDateException extends DomainException {
    public ConcertDateException(DomainErrorCode errorCode) {
        super(errorCode);
    }
}

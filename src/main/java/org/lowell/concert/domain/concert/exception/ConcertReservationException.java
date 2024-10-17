package org.lowell.concert.domain.concert.exception;

import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainException;

public class ConcertReservationException extends DomainException {
    public ConcertReservationException(DomainErrorCode errorCode) {
        super(errorCode);
    }
}

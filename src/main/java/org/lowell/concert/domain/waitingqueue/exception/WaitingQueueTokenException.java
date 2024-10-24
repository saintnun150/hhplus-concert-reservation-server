package org.lowell.concert.domain.waitingqueue.exception;

import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainException;

public class WaitingQueueTokenException extends DomainException {
    public WaitingQueueTokenException(DomainErrorCode errorCode) {
        super(errorCode);
    }
}

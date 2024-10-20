package org.lowell.concert.domain.waitingqueue.exception;

import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainException;

public class WaitingQueueException extends DomainException {

    public static WaitingQueueException create(WaitingQueueErrorCode errorCode) {
        return new WaitingQueueException(errorCode);
    }

    public WaitingQueueException(DomainErrorCode errorCode) {
        super(errorCode);
    }
}

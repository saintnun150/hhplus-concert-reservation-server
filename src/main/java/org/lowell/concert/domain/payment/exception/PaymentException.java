package org.lowell.concert.domain.payment.exception;

import org.lowell.concert.domain.common.exception.DomainErrorCode;
import org.lowell.concert.domain.common.exception.DomainException;

public class PaymentException extends DomainException {
    public PaymentException(DomainErrorCode errorCode) {
        super(errorCode);
    }
}

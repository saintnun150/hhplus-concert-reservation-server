package org.lowell.concert.application.common.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ApplicationError applicationError;

    public ApplicationException(ApplicationError applicationError) {
        super(applicationError.getMessage());
        this.applicationError = applicationError;
    }

    public static ApplicationException create(ApplicationError applicationError) {
        return new ApplicationException(applicationError);
    }
}


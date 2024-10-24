package org.lowell.concert.interfaces.api.common.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainError;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ApiErrorResponse {
    private final DomainError code;
    private final String message;
    private final Map<String, Object> payload;

    public static ApiErrorResponse of(DomainError error, String message, Map<String, Object> payload) {
        return new ApiErrorResponse(error, message, payload);
    }

}

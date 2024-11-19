package org.lowell.apps.common.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lowell.apps.common.exception.DomainError;

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

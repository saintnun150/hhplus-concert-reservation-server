package org.lowell.concert.domain.common.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class DomainException extends RuntimeException {
    private final DomainError domainError;
    private final Map<String, Object> payload;

    public DomainException(DomainError domainError) {
        super(domainError.getMessage());
        this.domainError = domainError;
        this.payload = Collections.emptyMap();
    }

    public DomainException(DomainError domainError, Map<String, Object> payload) {
        super(domainError.getMessage());
        this.domainError = domainError;
        this.payload = payload;
    }

    public DomainException(DomainError domainError, Map<String, Object> payload, Throwable cause) {
        super(domainError.getMessage(), cause);
        this.domainError = domainError;
        this.payload = payload;
    }

    public static DomainException create(DomainError domainError) {
        return new DomainException(domainError);
    }

    public static DomainException create(DomainError domainError, Map<String, Object> payload) {
        return new DomainException(domainError, payload);
    }

    public static Map<String, Object> createPayload(Object... objects) {
        Map<String, Object> payload = new HashMap<>();

        for (int i = 0; i < objects.length; i++) {
            String key = "p" + (i + 1);
            payload.put(key, objects[i]);
        }
        return Collections.unmodifiableMap(payload);
    }
}


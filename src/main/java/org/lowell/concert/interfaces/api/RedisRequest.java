package org.lowell.concert.interfaces.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RedisRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Set {
        private String key;
        private String value;
    }
}

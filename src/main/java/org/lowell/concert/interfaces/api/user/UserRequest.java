package org.lowell.concert.interfaces.api.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserRequest {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ChargeAccount {
        private final long balance;

    }
}

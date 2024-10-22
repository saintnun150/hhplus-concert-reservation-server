package org.lowell.concert.application.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public class UserInfo {

    @Getter
    @RequiredArgsConstructor
    public static class AccountInfo {
        private final Long accountId;
        private final Long userId;
        private final String username;
        private final Long balance;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;
    }
}

package org.lowell.concert.domain.user.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserAccountInfo {
    private Long userAccountId;
    private Long userId;
    private Long balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void changeBalance(TransactionType type, long amount) {
        if (type == TransactionType.CHARGE) {
            this.balance += amount;
        }
        this.balance -= amount;

    }
}

package org.lowell.concert.domain.user.dto;

import org.lowell.concert.domain.user.model.TransactionType;

public class UserAccountCommand {
    public record Action(Long userId, long amount) {}
}

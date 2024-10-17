package org.lowell.concert.domain.user.dto;

import org.lowell.concert.domain.user.model.TransactionType;

public class UserAccountCommand {
    public record Action(Long accountId, long amount, TransactionType transactionType) {}
    public record Update(Long accountId, long balance) {}
}

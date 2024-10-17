package org.lowell.concert.domain.user.dto;

import org.lowell.concert.domain.user.model.TransactionType;

public class UserAccountHistoryCommand {
    public record Create(Long accountId, long amount, TransactionType transactionType) {}
}

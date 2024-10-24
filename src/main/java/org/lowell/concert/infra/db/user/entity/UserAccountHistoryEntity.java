package org.lowell.concert.infra.db.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lowell.concert.domain.user.model.TransactionType;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_user_account_history")
@Getter
@NoArgsConstructor
public class UserAccountHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "account_id")
    private Long userAccountId;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Builder
    public UserAccountHistoryEntity(Long userAccountId, Long amount, LocalDateTime createdAt, TransactionType transactionType) {
        this.userAccountId = userAccountId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.transactionType = transactionType;
    }
}

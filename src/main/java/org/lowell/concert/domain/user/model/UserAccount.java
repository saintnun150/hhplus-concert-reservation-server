package org.lowell.concert.domain.user.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.user.exception.UserAccountErrorCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_user_account")
@Getter
@NoArgsConstructor
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public UserAccount(Long userId, Long balance) {
        this.userId = userId;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
    }

    public void chargeBalance(Long balance) {
        if (balance <= 0) {
            throw new DomainException(UserAccountErrorCode.INVALID_AMOUNT);
        }
        this.balance += balance;
        this.updatedAt = LocalDateTime.now();
    }

    public void useBalance(Long balance) {
        if (balance <= 0) {
            throw new DomainException(UserAccountErrorCode.INVALID_AMOUNT);
        }

        if (this.balance < balance) {
            throw new DomainException(UserAccountErrorCode.EXCEED_BALANCE);
        }
        this.balance -= balance;
        this.updatedAt = LocalDateTime.now();
    }

    public static UserAccount createAccount(Long userId) {
        return UserAccount.builder()
                          .userId(userId)
                          .balance(0L)
                          .build();
    }
}

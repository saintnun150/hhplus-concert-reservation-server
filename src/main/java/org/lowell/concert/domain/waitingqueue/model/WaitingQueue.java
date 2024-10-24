package org.lowell.concert.domain.waitingqueue.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueError;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_waiting_queue")
@Getter
@NoArgsConstructor
public class WaitingQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Column(name = "token")
    private String token;

    @Column(name = "status")
    private TokenStatus tokenStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Builder
    public WaitingQueue(Long tokenId, String token, TokenStatus tokenStatus, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime expiresAt) {
        if (!StringUtils.hasText(token)) {
            throw DomainException.create(WaitingQueueError.INVALID_TOKEN_INPUT);
        }
        if (tokenStatus == null || !EnumUtils.isValidEnum(TokenStatus.class, tokenStatus.name())) {
            throw DomainException.create(WaitingQueueError.INVALID_TOKEN_STATUS_INPUT);
        }

        this.tokenId = tokenId;
        this.token = token;
        this.tokenStatus = tokenStatus;
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.expiresAt = expiresAt;
    }

    public void validateWaitingStatus() {
        if (tokenStatus != TokenStatus.WAITING) {
            throw DomainException.create(WaitingQueueError.NOT_WAITING_STATUS);
        }
    }

    public void validateActivateStatus() {
        if (tokenStatus != TokenStatus.ACTIVATE) {
            throw DomainException.create(WaitingQueueError.NOT_ACTIVATE_STATUS);
        }
    }

    public void validateTokenExpiredDate(LocalDateTime now, long timeToLive) {
        validateActivateStatus();
        if (expiresAt == null || now.isAfter(expiresAt.plusMinutes(timeToLive))) {
            throw DomainException.create(WaitingQueueError.TOKEN_EXPIRED);
        }
    }

    public void activateToken(LocalDateTime now, long timeToLive) {
        validateWaitingStatus();
        tokenStatus = TokenStatus.ACTIVATE;
        this.updatedAt = now;
        this.expiresAt = now.plusMinutes(timeToLive);
    }

    public void expiredToken(LocalDateTime now, long timeToLive) {
        validateTokenExpiredDate(now, timeToLive);
        this.tokenStatus = TokenStatus.EXPIRED;
        this.updatedAt = now;
    }

}

package org.lowell.concert.domain.waitingqueue.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueErrorCode;
import org.lowell.concert.domain.waitingqueue.exception.WaitingQueueException;
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
            throw WaitingQueueException.create(WaitingQueueErrorCode.INVALID_TOKEN_INPUT);
        }
        if (tokenStatus == null || !EnumUtils.isValidEnum(TokenStatus.class, tokenStatus.name())) {
            throw WaitingQueueException.create(WaitingQueueErrorCode.INVALID_TOKEN_STATUS_INPUT);
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
            throw WaitingQueueException.create(WaitingQueueErrorCode.NOT_WAITING_STATUS);
        }
    }

    public void validateActivateStatus() {
        if (tokenStatus != TokenStatus.ACTIVATE) {
            throw WaitingQueueException.create(WaitingQueueErrorCode.NOT_ACTIVATE_STATUS);
        }

    }

    public void validateTokenExpiredDate(long timeToLive) {
        validateActivateStatus();
        if (expiresAt == null || LocalDateTime.now().isAfter(expiresAt.plusMinutes(timeToLive))) {
            throw WaitingQueueException.create(WaitingQueueErrorCode.TOKEN_EXPIRED);
        }
    }

}

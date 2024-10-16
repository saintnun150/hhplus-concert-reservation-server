package org.lowell.concert.infra.db.waitingqueue;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_waiting_queue_token")
@Getter
@NoArgsConstructor
public class WaitingQueueTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    public WaitingQueueTokenEntity(Long tokenId, String token, TokenStatus tokenStatus, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime expiresAt) {
        this.tokenId = tokenId;
        this.token = token;
        this.tokenStatus = tokenStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.expiresAt = expiresAt;
    }

}

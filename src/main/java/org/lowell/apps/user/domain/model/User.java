package org.lowell.apps.user.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_user")
@Getter
@NoArgsConstructor
public class User {

    @Builder
    public User(Long userId, String username, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

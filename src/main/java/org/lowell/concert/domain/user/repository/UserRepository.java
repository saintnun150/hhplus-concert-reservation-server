package org.lowell.concert.domain.user.repository;

import org.lowell.concert.domain.user.model.User;

import java.util.Optional;

public interface UserRepository {
    void createUser(Long userId, String username);
    void deleteAll();
    Optional<User> getUser(Long userId);
}

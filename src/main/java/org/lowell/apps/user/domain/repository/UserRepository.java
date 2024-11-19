package org.lowell.apps.user.domain.repository;

import org.lowell.apps.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    void createUser(Long userId, String username);
    void deleteAll();
    Optional<User> getUser(Long userId);
}

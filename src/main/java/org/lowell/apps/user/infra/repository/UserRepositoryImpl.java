package org.lowell.apps.user.infra.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.user.domain.model.User;
import org.lowell.apps.user.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpaRepository;

    @Transactional
    @Override
    public void createUser(Long userId, String username) {
        jpaRepository.save(User.builder()
                               .userId(userId)
                               .username(username)
                               .build());
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return jpaRepository.findById(userId);
    }
}

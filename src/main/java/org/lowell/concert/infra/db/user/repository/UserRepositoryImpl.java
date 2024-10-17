package org.lowell.concert.infra.db.user.repository;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.user.UserMapper;
import org.lowell.concert.domain.user.model.UserInfo;
import org.lowell.concert.domain.user.repository.UserRepository;
import org.lowell.concert.infra.db.user.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;

    @Override
    public UserInfo getUserInfo(Long userId) {
        UserEntity userEntity = jpaRepository.findById(userId)
                                             .orElse(null);
        return userMapper.toPojo(userEntity);
    }

    @Override
    public Optional<UserInfo> getUserInfoWithOptional(Long userId) {
        return jpaRepository.findById(userId)
                            .map(userMapper::toPojo);
    }
}

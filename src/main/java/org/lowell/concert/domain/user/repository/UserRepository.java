package org.lowell.concert.domain.user.repository;

import org.lowell.concert.domain.user.model.UserInfo;

import java.util.Optional;

public interface UserRepository {
    UserInfo getUserInfo(Long userId);
    Optional<UserInfo> getUserInfoWithOptional(Long userId);
}

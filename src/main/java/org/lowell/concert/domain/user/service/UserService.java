package org.lowell.concert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.user.exception.UserErrorCode;
import org.lowell.concert.domain.user.exception.UserException;
import org.lowell.concert.domain.user.model.UserInfo;
import org.lowell.concert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfo getUser(Long userId){
        UserInfo userInfo = userRepository.getUserInfo(userId);
        if (userInfo == null){
            throw new UserException(UserErrorCode.NOT_FOUND_USER);
        }
        return userInfo;
    }
}

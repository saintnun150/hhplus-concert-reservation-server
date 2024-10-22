package org.lowell.concert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.domain.common.exception.DomainException;
import org.lowell.concert.domain.user.exception.UserErrorCode;
import org.lowell.concert.domain.user.model.User;
import org.lowell.concert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void createUser(Long userId, String username){
        userRepository.createUser(userId, username);
    }

    public void deleteAllUser(){
        userRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId){
        User user = userRepository.getUser(userId)
                                  .orElseThrow(() -> new DomainException(UserErrorCode.NOT_FOUND_USER));
        return user;
    }
}

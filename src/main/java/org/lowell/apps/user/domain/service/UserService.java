package org.lowell.apps.user.domain.service;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.user.domain.exception.UserError;
import org.lowell.apps.user.domain.model.User;
import org.lowell.apps.user.domain.repository.UserRepository;
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
                                  .orElseThrow(() -> DomainException.create(UserError.NOT_FOUND_USER,
                                                                            DomainException.createPayload(userId)));

        return user;
    }
}

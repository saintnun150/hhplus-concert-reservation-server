package org.lowell.apps.domain.user.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.apps.common.exception.DomainException;
import org.lowell.apps.user.domain.exception.UserError;
import org.lowell.apps.user.domain.repository.UserRepository;
import org.lowell.apps.user.domain.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @DisplayName("없는 사용자 ID로 요청하면 예외가 발생한다.")
    @Test
    void throwException_when_request_with_wrong_userId() {
        Long userId = 0L;
        when(userRepository.getUser(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(userId))
                .isInstanceOfSatisfying(DomainException.class, ex -> {
                    assertThat(ex.getDomainError()).isEqualTo(UserError.NOT_FOUND_USER);
                });
    }
}
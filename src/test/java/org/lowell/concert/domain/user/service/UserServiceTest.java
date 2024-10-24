package org.lowell.concert.domain.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lowell.concert.domain.user.exception.UserErrorCode;
import org.lowell.concert.domain.user.exception.UserException;
import org.lowell.concert.domain.user.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(userRepository.getUserInfo(userId)).thenReturn(null);

        assertThatThrownBy(() -> userService.getUser(userId))
                .isInstanceOfSatisfying(UserException.class, ex -> {
                    assertThat(ex.getErrorCode()).isEqualTo(UserErrorCode.NOT_FOUND_USER);
                });
    }
}
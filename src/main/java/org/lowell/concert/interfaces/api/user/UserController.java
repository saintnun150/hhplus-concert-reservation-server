package org.lowell.concert.interfaces.api.user;

import org.lowell.concert.interfaces.api.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserApiDocs {


    @GetMapping("/{userId}/accounts")
    public ApiResponse<UserResponse.Info> getUserAccount(@PathVariable String userId) {
        return ApiResponse.createOk(UserResponse.Info.builder()
                                                     .accountId(10L)
                                                     .userId(1L)
                                                     .balance(10_000L)
                                                     .createdAt(LocalDateTime.now().minusDays(10L))
                                                     .updatedAt(LocalDateTime.now().minusDays(5L))
                                                     .build());
    }

    @PatchMapping("/{userId}/accounts")
    public ApiResponse<UserResponse.Info> chargeUserAccount(@PathVariable String userId,
                                                            UserRequest.ChargeAccount request) {
        return ApiResponse.createOk(UserResponse.Info.builder()
                                                     .accountId(10L)
                                                     .userId(1L)
                                                     .balance(20_000L)
                                                     .createdAt(LocalDateTime.now().minusDays(10L))
                                                     .updatedAt(LocalDateTime.now())
                                                     .build());
    }
}

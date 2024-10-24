package org.lowell.concert.interfaces.api.user;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.user.UserFacade;
import org.lowell.concert.application.user.UserInfo;
import org.lowell.concert.interfaces.api.common.support.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserApiDocs {
    private final UserFacade userFacade;

    @GetMapping("/{userId}/accounts")
    public ApiResponse<UserResponse.Info> getUserAccount(@PathVariable Long userId) {
        UserInfo.AccountInfo accountInfo = userFacade.getAccountInfo(userId);
        return ApiResponse.createOk(UserResponse.Info.builder()
                                                     .accountId(accountInfo.getAccountId())
                                                     .userId(accountInfo.getUserId())
                                                     .balance(accountInfo.getBalance())
                                                     .createdAt(accountInfo.getCreatedAt())
                                                     .updatedAt(accountInfo.getUpdatedAt())
                                                     .build());
    }

    @PatchMapping("/{userId}/accounts")
    public ApiResponse<UserResponse.Info> chargeUserAccount(@PathVariable Long userId,
                                                            UserRequest.ChargeAccount request) {
        UserInfo.AccountInfo accountInfo = userFacade.chargeBalance(userId, request.getBalance());
        return ApiResponse.createOk(UserResponse.Info.builder()
                                                     .accountId(accountInfo.getAccountId())
                                                     .userId(accountInfo.getUserId())
                                                     .balance(accountInfo.getBalance())
                                                     .createdAt(accountInfo.getCreatedAt())
                                                     .updatedAt(accountInfo.getUpdatedAt())
                                                     .build());
    }
}

package org.lowell.apps.user.interfaces.api;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.user.application.UserFacade;
import org.lowell.apps.user.application.UserInfo;
import org.lowell.apps.common.api.ApiResponse;
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

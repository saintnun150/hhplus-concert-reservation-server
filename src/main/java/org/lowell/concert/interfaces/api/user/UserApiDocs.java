package org.lowell.concert.interfaces.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.lowell.concert.interfaces.api.common.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "사용자 API", description = "사용자 관련 API")
public interface UserApiDocs {

    @Operation(summary = "계좌 정보 조회", description = "사용자 ID를 통해 해당 계좌 정보를 조회한다.")
    ApiResponse<UserResponse.Info> getUserAccount(@PathVariable Long userId);

    @Operation(summary = "계좌 잔액 충전 ", description = "사용자 ID를 통해 해당 계좌에 잔액을 충전한다.")
    ApiResponse<UserResponse.Info> chargeUserAccount(@PathVariable Long userId,
                                                     UserRequest.ChargeAccount request);
}

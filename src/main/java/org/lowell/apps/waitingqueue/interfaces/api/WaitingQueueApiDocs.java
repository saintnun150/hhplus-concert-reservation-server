package org.lowell.apps.waitingqueue.interfaces.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.lowell.apps.common.api.ApiResponse;

@Tag(name = "대기열 토큰 API", description = "대기열 토큰 관련 API")
public interface WaitingQueueApiDocs {

    @Operation(summary = "대기열 토큰 발급", description = "대기열 토큰을 반환하거나 생성후 토큰 정보를 반환")
    ApiResponse<WaitingQueueResponse.TokenInfo> createQueueToken();

    @Operation(summary = "대기열 토큰 상태 조회", description = "token으로 조회하여 현재 대기열 상태 정보 반환")
    ApiResponse<WaitingQueueResponse.TokenInfo> getQueueTokenOrder(@Parameter(hidden = true) String tokenStr);
}
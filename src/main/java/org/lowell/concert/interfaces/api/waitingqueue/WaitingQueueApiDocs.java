package org.lowell.concert.interfaces.api.waitingqueue;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.lowell.concert.interfaces.api.common.ApiResponse;

@Tag(name = "대기열 토큰 API", description = "대기열 토큰 관련 API")
public interface WaitingQueueApiDocs {

    @Operation(summary = "대기열 토큰 발급", description = "사용자 ID를 통해 대기열 토큰을 반환하거나 생성후 토큰 정보를 반환")
    ApiResponse<WaitingQueueResponse.QueueInfo> createQueue(WaitingQueueRequest.CreateQueue request);

    @Operation(summary = "대기열 토큰 상태 조회", description = "token으로 조회하여 현재 대기열 상태 정보 반환")
    ApiResponse<WaitingQueueResponse.QueueOrderInfo> getQueueOrder(@Parameter(hidden = true) String tokenStr);
}
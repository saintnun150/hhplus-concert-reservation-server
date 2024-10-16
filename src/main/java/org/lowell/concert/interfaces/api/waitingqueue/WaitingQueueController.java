package org.lowell.concert.interfaces.api.waitingqueue;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.lowell.concert.domain.waitingqueue.model.TokenStatus;
import org.lowell.concert.interfaces.api.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequestMapping("/api/v1/waiting-queues")
@RestController
public class WaitingQueueController implements WaitingQueueApiDocs {

    @PostMapping("/tokens")
    public ApiResponse<WaitingQueueResponse.CreatedQueueInfo> createQueue(WaitingQueueRequest.CreateQueue request) {
        WaitingQueueResponse.CreatedQueueInfo token =
                WaitingQueueResponse.CreatedQueueInfo.builder()
                                                     .tokenId(5L)
                                                     .token("uuid-token1")
                                                     .waitingQueueStatus(TokenStatus.WAITING.name())
                                                     .build();
        return ApiResponse.createOk(token);
    }

    @SecurityRequirement(name = "queueToken")
    @GetMapping("/tokens")
    public ApiResponse<WaitingQueueResponse.CurrentQueueInfo> getCurrentQueue(@RequestHeader("X-QUEUE-TOKEN") String token) {
        WaitingQueueResponse.CurrentQueueInfo info = WaitingQueueResponse.CurrentQueueInfo.builder()
                                                                                           .tokenId(20L)
                                                                                           .remainQueueCount(1)
                                                                                           .waitingQueueStatus(TokenStatus.WAITING.name())
                                                                                           .expiredDate(LocalDateTime.now().plusMinutes(5))
                                                                                           .build();
        return ApiResponse.createOk(info);
    }
}

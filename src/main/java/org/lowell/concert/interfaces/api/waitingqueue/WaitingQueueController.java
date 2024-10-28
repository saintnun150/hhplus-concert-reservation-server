package org.lowell.concert.interfaces.api.waitingqueue;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lowell.concert.application.waitingqueue.WaitingQueueFacade;
import org.lowell.concert.application.waitingqueue.WaitingQueueInfo;
import org.lowell.concert.interfaces.api.common.support.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/waiting-queues")
@RestController
@RequiredArgsConstructor
public class WaitingQueueController implements WaitingQueueApiDocs {
    private final WaitingQueueFacade waitingQueueFacade;

    @PostMapping("")
    public ApiResponse<WaitingQueueResponse.QueueInfo> createQueue() {
        WaitingQueueInfo.Get waitingQueue = waitingQueueFacade.createWaitingQueue();
        WaitingQueueResponse.QueueInfo token = WaitingQueueResponse.QueueInfo.builder()
                                                                             .tokenId(waitingQueue.getTokenId())
                                                                             .token(waitingQueue.getToken())
                                                                             .waitingQueueStatus(waitingQueue.getTokenStatus())
                                                                             .build();
        return ApiResponse.createOk(token);
    }

    @SecurityRequirement(name = "queueToken")
    @GetMapping("/tokens")
    public ApiResponse<WaitingQueueResponse.QueueOrderInfo> getQueueOrder(@RequestHeader("X-QUEUE-TOKEN") String token) {
        WaitingQueueInfo.GetWithOrder waitingQueueOrder = waitingQueueFacade.getWaitingQueueOrder(token);
        WaitingQueueResponse.QueueOrderInfo info = WaitingQueueResponse.QueueOrderInfo.builder()
                                                                                      .tokenId(waitingQueueOrder.getWaitingQueue().getTokenId())
                                                                                      .remainQueueCount(waitingQueueOrder.getOrder().intValue())
                                                                                      .waitingQueueStatus(waitingQueueOrder.getWaitingQueue().getTokenStatus())
                                                                                      .expiredDate(waitingQueueOrder.getWaitingQueue().getExpiresAt())
                                                                                      .build();
        return ApiResponse.createOk(info);
    }
}

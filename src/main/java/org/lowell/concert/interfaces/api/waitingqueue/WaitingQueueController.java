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
    public ApiResponse<WaitingQueueResponse.TokenInfo> createQueueToken() {
        WaitingQueueInfo.Get waitingQueue = waitingQueueFacade.createWaitingQueue();
        return ApiResponse.createOk(WaitingQueueResponse.TokenInfo.builder()
                                                                  .token(waitingQueue.getToken())
                                                                  .order(waitingQueue.getOrder())
                                                                  .build());
    }

    @SecurityRequirement(name = "queueToken")
    @GetMapping("/tokens")
    public ApiResponse<WaitingQueueResponse.TokenInfo> getQueueTokenOrder(@RequestHeader("X-QUEUE-TOKEN") String token) {
        WaitingQueueInfo.Get order = waitingQueueFacade.getWaitingQueueOrder(token);
        return ApiResponse.createOk(WaitingQueueResponse.TokenInfo.builder()
                                                                  .token(order.getToken())
                                                                  .order(order.getOrder())
                                                                  .build());
    }
}

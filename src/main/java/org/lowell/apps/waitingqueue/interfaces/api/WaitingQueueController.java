package org.lowell.apps.waitingqueue.interfaces.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.lowell.apps.waitingqueue.application.WaitingQueueFacade;
import org.lowell.apps.waitingqueue.application.WaitingQueueInfo;
import org.lowell.apps.common.api.ApiResponse;
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

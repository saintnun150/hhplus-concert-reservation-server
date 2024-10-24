package org.lowell.concert.interfaces.api.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lowell.concert.application.waitingqueue.WaitingQueueFacade;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingQueueInterceptor implements HandlerInterceptor {
    private final WaitingQueueFacade waitingQueueFacade;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-Queue-Token");
        waitingQueueFacade.checkWaitingQueueActivate(token, LocalDateTime.now());
        return true;
    }
}

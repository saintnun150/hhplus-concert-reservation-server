package org.lowell.concert.interfaces.api.common.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GlobalLoggingInterceptor implements HandlerInterceptor {

    @Value("${concert.api.logging.enable:true}")
    private boolean loggingEnabled;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // request 정보 로깅
        if (!loggingEnabled) {
            return true;
        }
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String token = request.getHeader("X-Queue-Token");
        String remoteAddr = request.getRemoteAddr();

        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getValue().length > 1) {
                params.put(entry.getKey(), entry.getValue());
            } else {
                params.put(entry.getKey(), entry.getValue()[0]);
            }
        }
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("requestURI", requestURI);
        logMap.put("method", method);
        logMap.put("token", token);
        logMap.put("remoteAddr", remoteAddr);
        logMap.put("parameters", params);

        log.info("## GlobalLogging Request logMap: {}", toJson(logMap));

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        HandlerInterceptor.super.postHandle(request, responseWrapper, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!loggingEnabled) {
            return;
        }

        // response 정보 로깅
        ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
        byte[] responseArray = responseWrapper.getContentAsByteArray();

        String responseBody = new String(responseArray, responseWrapper.getCharacterEncoding());
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("responseStatus", response.getStatus());
        logMap.put("responseBody", responseBody);

        log.info("## GlobalLogging Response logMap: {}", toJson(logMap));
        responseWrapper.copyBodyToResponse();
    }

    private String toJson(Map<String, Object> map) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error("## Failed to convert log map to JsonString", e);
            return map.toString();
        }
    }
}

package org.lowell.concert.interfaces.api.common.config;

import lombok.RequiredArgsConstructor;
import org.lowell.concert.interfaces.api.common.interceptor.GlobalLoggingInterceptor;
import org.lowell.concert.interfaces.api.common.interceptor.WaitingQueueInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final GlobalLoggingInterceptor loggingInterceptor;
    private final WaitingQueueInterceptor waitingQueueInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/error");

        registry.addInterceptor(waitingQueueInterceptor)
                .addPathPatterns(getWaitingFilterUrlPatterns());
    }

    private String[] getWaitingFilterUrlPatterns() {
        String basePath = "/api/v1";
        List<String> endpoints = Arrays.asList("/payments/*", "/concerts/*", "/waiting-queues/tokens");
        return endpoints.stream()
                        .map(endpoint -> basePath + endpoint)
                        .toArray(String[]::new);
    }
}

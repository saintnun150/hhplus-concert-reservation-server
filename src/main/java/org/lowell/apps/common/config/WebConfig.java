package org.lowell.apps.common.config;

import lombok.RequiredArgsConstructor;
import org.lowell.apps.common.interceptor.GlobalLoggingInterceptor;
import org.lowell.apps.waitingqueue.interfaces.interceptor.WaitingQueueInterceptor;
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
                .addPathPatterns(getWaitingFilterUrlPatterns())
                .excludePathPatterns("/api/v1/waiting-queues", "/api/v1/waiting-queues/**");
    }

    private String[] getWaitingFilterUrlPatterns() {
        String basePath = "/api/v1";
        List<String> endpoints = Arrays.asList("/payments/*", "/concerts/*");
        return endpoints.stream()
                        .map(endpoint -> basePath + endpoint)
                        .toArray(String[]::new);
    }
}

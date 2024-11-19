package org.lowell.apps.common.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lowell.apps.waitingqueue.interfaces.filter.WaitingQueueFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class FilterConfig {
    private final WaitingQueueFilter waitingQueueFilter;

    public FilterConfig(WaitingQueueFilter waitingQueueFilter) {
        this.waitingQueueFilter = waitingQueueFilter;
    }

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> requestAndResponseWrappingFilter() {
        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
                ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

                try {
                    filterChain.doFilter(wrappedRequest, wrappedResponse);
                } finally {
                    wrappedResponse.copyBodyToResponse();
                }
            }
        });
        registrationBean.setOrder(0);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<WaitingQueueFilter> filterRegistrationBean() {
        FilterRegistrationBean<WaitingQueueFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(waitingQueueFilter);
        registrationBean.setName("waitingQueueFilter");
        registrationBean.addUrlPatterns(getFilterUrlPatterns());
        registrationBean.setOrder(1);
        return registrationBean;
    }

    private String[] getFilterUrlPatterns() {
        String basePath = "/api/v1";
        List<String> endpoints = Arrays.asList("/payments/*", "/concerts/*", "/waiting-queues/tokens");
        return endpoints.stream()
                        .map(endpoint -> basePath + endpoint)
                        .toArray(String[]::new);
    }
}

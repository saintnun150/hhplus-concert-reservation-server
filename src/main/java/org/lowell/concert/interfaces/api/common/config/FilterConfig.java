package org.lowell.concert.interfaces.api.common.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Configuration
public class FilterConfig {
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
}

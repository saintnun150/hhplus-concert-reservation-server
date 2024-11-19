package org.lowell.apps.waitingqueue.interfaces.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class WaitingQueueFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    public WaitingQueueFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("## WaitingQueueFilter doFilterInternal");

        String token = request.getHeader("X-Queue-Token");
        if (!StringUtils.hasText(token)) {
            handleFilterError(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void handleFilterError(HttpServletResponse response) {
        response.setStatus(FilterError.INVALID_TOKEN.statusCode.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try {
            String errorString = objectMapper.writeValueAsString(FilterError.INVALID_TOKEN.getMessage());
            response.getWriter().write(errorString);
        } catch (Exception e) {
            log.error("## Failed to write WaitingQueue", e);
        }
    }

    @Getter
    private enum FilterError {
        INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),;

        private final HttpStatus statusCode;
        private final String message;

        FilterError(HttpStatus statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
        }
    }
}

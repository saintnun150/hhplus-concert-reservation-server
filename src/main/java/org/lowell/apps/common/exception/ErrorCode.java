package org.lowell.apps.common.exception;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum ErrorCode implements DomainError {
    NOT_FOUND("리소스를 찾을 수 없습니다."),
    UNAUTHORIZED("인증이 필요합니다."),
    INTERNAL_SERVER("내부 서버 오류가 발생했습니다."),
    FORBIDDEN("접근이 금지되었습니다."),
    BAD_REQUEST("잘못된 요청입니다."),
    VALIDATION("요청 값 검증에 실패했습니다.");


    private final String defaultMessage;

    ErrorCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public ErrorCode getCode() {
        return this;
    }

    @Override
    public String getMessage() {
        return defaultMessage;
    }

    @Override
    public LogLevel getLogLevel() {
        return LogLevel.ERROR;
    }
}

package org.lowell.concert.domain.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
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

}

package org.lowell.concert.domain.common.exception;

public record DomainErrorResponse(Integer status, String code, String message) {
}

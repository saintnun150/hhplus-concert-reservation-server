package org.lowell.concert.interfaces.api.common.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private final int statusCode;
    private final T data;


    public static <T> ApiResponse<T> createOk() {
        return new ApiResponse<T>(HttpStatus.OK.value(), null);
    }

    public static <T> ApiResponse<T> createOk(final T data) {
        return new ApiResponse<T>(HttpStatus.OK.value(), data);
    }

}

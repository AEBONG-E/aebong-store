package com.aebong.store.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private String code;
    private String message;
    private T data;

    // 응답 성공 경우 (데이터 X)
    public static ApiResponse<Void> success() {
        return ApiResponse.<Void>builder()
                .code("SUCCESS")
                .data(null)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code("SUCCESS")
                .data(data)
                .build();
    }

    // 응답 실패 경우 (데이터 O)
    public static <T> ApiResponse<T> error(String errorCode, String errorMessage) {
        return ApiResponse.<T>builder()
                .code(errorCode)
                .message(errorMessage)
                .data(null)
                .build();
    }

}

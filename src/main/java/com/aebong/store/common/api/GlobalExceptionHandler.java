package com.aebong.store.common.api;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.exceptions.UserApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserApplicationException.class)
    public ResponseEntity<?> handleException(UserApplicationException e) {
        log.error(">>>>> [UserApplicationException] ERROR: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorType().getStatus())
                .body(ApiResponse.error(e.getErrorType().name(), e.getErrorType().getMessageKr()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException e) {
        log.error(">>>>> [RuntimeException] ERROR: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(CustomErrorType.INTERNAL_SERVER_ERROR.name(), CustomErrorType.INTERNAL_SERVER_ERROR.getMessageKr()));
    }

}

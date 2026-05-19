package com.aebong.store.common.exceptions;

import com.aebong.store.common.enums.CustomErrorType;
import lombok.Getter;

@Getter
public class OrderApplicationException extends RuntimeException {

    private final CustomErrorType errorType;

    public OrderApplicationException(CustomErrorType errorType, String msg) {
        super(msg);
        this.errorType = errorType;
    }
}

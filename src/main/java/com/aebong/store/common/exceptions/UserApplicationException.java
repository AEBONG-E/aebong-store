package com.aebong.store.common.exceptions;

import com.aebong.store.common.enums.CustomErrorType;
import lombok.Getter;

/**
 * todo: 추후 커스텀 에러코드 + http 상태값 코드 생성 시 커스텀 예외 세분화를 위한 리팩토링 필요
 */
@Getter
public class UserApplicationException extends RuntimeException {

    private CustomErrorType errorType;
    private String msg;


    public UserApplicationException() {
    }

    public UserApplicationException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public UserApplicationException(CustomErrorType errorType, String msg) {
        super(msg);
        this.errorType = errorType;
        this.msg = msg;
    }

}

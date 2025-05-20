package com.aebong.store.common.exceptions;

import lombok.Getter;

/**
 * todo: 추후 커스텀 에러코드 + http 상태값 코드 생성 시 커스텀 예외 세분화를 위한 리팩토링 필요
 */
@Getter
public class UserInvalidException extends RuntimeException {

    public UserInvalidException() {
    }

    public UserInvalidException(String message) {
        super("Invalid user data.");
    }

    public UserInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}

package com.aebong.store.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorType {

    NOT_FOUND_USER          (HttpStatus.NOT_FOUND,              "not found user.",         "사용자를 찾을 수 없습니다."),
    IS_EXIST_USER           (HttpStatus.CONFLICT,               "is exist user.",          "존재하는 사용자입니다."),
    INTERNAL_SERVER_ERROR   (HttpStatus.INTERNAL_SERVER_ERROR,  "internal server error.",  "서버 내부 오류입니다.")
    ;

    private final HttpStatus status;
    private final String message;
    private final String messageKr;

}

package com.aebong.store.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorType {

    /** user domain */
    NOT_FOUND_USER          (HttpStatus.CONFLICT,              "not found user.",         "사용자를 찾을 수 없습니다."),
    IS_EXIST_USER           (HttpStatus.CONFLICT,              "is exist user.",          "존재하는 사용자입니다."),

    /** product domain */
    IS_EXIST_PRODUCT_NAME   (HttpStatus.CONFLICT,              "is exist product name.",  "존재하는 상품명입니다."),
    IS_EXIST_PRODUCT_CODE   (HttpStatus.CONFLICT,              "is exist product code.",  "존재하는 상품 코드입니다."),
    INVALID_AMOUNT          (HttpStatus.CONFLICT,              "invalid amount.",         "유효하지 않은 금액입니다."),
    INVALID_IMAGE_FORMAT    (HttpStatus.CONFLICT,              "invalid image format.",   "허용되지 않는 이미지 형식입니다."),

    /** common */
    BAD_REQUEST             (HttpStatus.BAD_REQUEST,           "is an invalid request.",  "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR   (HttpStatus.INTERNAL_SERVER_ERROR, "internal server error.",  "서버 내부 오류입니다.")
    ;

    private final HttpStatus status;
    private final String message;
    private final String messageKr;

}

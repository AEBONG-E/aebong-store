-- ===========================================================================================================
-- 회원
-- ===========================================================================================================

CREATE TABLE `users`
(
    `user_id`                           BIGINT AUTO_INCREMENT COMMENT '회원순번 PK',
    `user_type`                         VARCHAR(20)                        NOT NULL COMMENT '회원유형',
    `user_account`                      VARCHAR(30)                        NOT NULL COMMENT '회원계정',
    `user_account_type`                 VARCHAR(20)                        NOT NULL COMMENT '회원계정 유형',
    `user_password`                     VARCHAR(100)                       NOT NULL COMMENT '비밀번호',
    `user_status`                       VARCHAR(20)                        NOT NULL COMMENT '회원상태 (활성, 탈퇴, 휴면, 잠금)',
    `password_init_yn`                  CHAR     DEFAULT 'N'               NOT NULL COMMENT '비밀번호 초기화 필요 여부',
    `fail_password_count`               INT      DEFAULT 0                 NOT NULL COMMENT '비밀번호 틀린 횟수',
    `account_locked_datetime`           DATETIME                           NULL COMMENT '로그인 잠금 일시',
    `last_login_datetime`               DATETIME                           NULL COMMENT '마지막 로그인 일시',
    `last_password_change_datetime`     DATETIME                           NOT NULL COMMENT '마지막 비밀번호 변경 일시',
    `required_password_change_datetime` DATETIME                           NOT NULL COMMENT '비밀번호 변경 요구 일시',
    `login_available_date`              DATE                               NULL COMMENT '로그인 가능일자',
    `rejoin_possible_date`              DATE                               NULL COMMENT '재가입 가능일자',
    `user_connecting_information_id`    BIGINT                             NULL COMMENT 'CI 인증정보 순번',
    `rejoin_yn`                         CHAR     DEFAULT 'N'               NOT NULL COMMENT '재가입여부',
    `delete_yn`                         CHAR     DEFAULT 'N'               NOT NULL,
    `created_user_id`                   BIGINT                             NOT NULL,
    `created_datetime`                  DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`                  BIGINT                             NULL,
    `modified_datetime`                 DATETIME                           NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB COMMENT '회원 관리 테이블';

CREATE TABLE `user_detail`
(
    `user_detail_id`         BIGINT AUTO_INCREMENT COMMENT '회원 상세정보 순번 PK',
    `user_id`                BIGINT       NOT NULL COMMENT '회원순번',
    `first_name`             VARCHAR(100) NOT NULL COMMENT '이름',
    `last_name`              VARCHAR(100) NOT NULL COMMENT '성',
    `birth_date`             DATE                  DEFAULT NULL COMMENT '생년월일',
    `gender`                 VARCHAR(20)  NOT NULL COMMENT '성별(MALE|FEMALE|NON_BINARY|OTHER)',
    `mobile_number`          VARCHAR(11)           DEFAULT NULL COMMENT '휴대폰번호',
    `personal_id`            VARCHAR(100)          DEFAULT NULL COMMENT '개인식별번호',
    `nick_name`              VARCHAR(100)          DEFAULT NULL COMMENT '회원 닉네임',
    `tel_number`             VARCHAR(30)           DEFAULT NULL COMMENT '전화번호',
    `email`                  VARCHAR(150)          DEFAULT NULL COMMENT '이메일주소',
    `address1`               VARCHAR(255)          DEFAULT NULL COMMENT '주소',
    `address2`               VARCHAR(255)          DEFAULT NULL COMMENT '상세주소',
    `city`                   VARCHAR(100)          DEFAULT NULL COMMENT '시',
    `state`                  VARCHAR(100)          DEFAULT NULL COMMENT '도/주',
    `zipcode`                VARCHAR(30)           DEFAULT NULL COMMENT '우편번호',
    `join_datetime`          DATETIME     NOT NULL COMMENT '회원가입 일시',
    `activated_datetime`     DATETIME     NOT NULL COMMENT '계정 활성화 일시 - 최초 가입시, 탈퇴 -> 복원 시, 휴면 -> 복구 시',
    `inactivated_datetime`   DATETIME              DEFAULT NULL COMMENT '계정 비활성 일시',
    `withdrawal_datetime`    DATETIME              DEFAULT NULL COMMENT '탈퇴 일시',
    `dormant_datetime`       DATETIME              DEFAULT NULL COMMENT '휴면처리 일시',
#     todo: 알림기능 개발 시 추가 예정
#     `send_email_state`       CHAR(1)               DEFAULT NULL COMMENT '가입완료 이메일 전송 여부 (NULL:제외, Y:발송완료, N:미발송, F:발송실패)',
#     `send_email_retry_count` TINYINT      NOT NULL DEFAULT '0' COMMENT '가입완료 이메일 전송 재시도 횟수',
#     `send_alarm_state`       CHAR(1)               DEFAULT NULL COMMENT '가입완료 알림톡 전송 여부 (NULL:제외, Y:발송완료, N:미발송, F:발송실패)',
#     `send_alarm_retry_count` TINYINT      NOT NULL DEFAULT '0' COMMENT '가입완료 알림톡 전송 재시도 횟수',
    `delete_yn`              VARCHAR(1)   NOT NULL,
    `created_user_id`        BIGINT       NOT NULL,
    `created_datetime`       DATETIME              DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`       BIGINT       NULL,
    `modified_datetime`      DATETIME     NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_detail_id`),
    CONSTRAINT `fk_user_detail_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB COMMENT ='회원 상세 정보 관리 테이블';

CREATE TABLE `user_connecting_information`
(
    `user_connecting_information_id` BIGINT AUTO_INCREMENT COMMENT '회원 실명인증 순번 PK',
    `request_number`                 VARCHAR(50)     NOT NULL COMMENT '요청번호',
    `response_number`                VARCHAR(50) DEFAULT NULL COMMENT '응답번호',
    `legal_name`                     VARCHAR(50) DEFAULT NULL COMMENT '실명',
    `birth_date`                     DATE        DEFAULT NULL COMMENT '법정생년월일',
    `gender_type`                    VARCHAR(20) DEFAULT NULL COMMENT '성별',
    `duplication_information`        VARCHAR(64) DEFAULT NULL COMMENT 'DI 값',
    `connecting_information`         VARCHAR(88) DEFAULT NULL COMMENT 'CI 값',
    `delete_yn`                      VARCHAR(1)      NOT NULL,
    `created_user_id`                BIGINT          NOT NULL,
    `created_datetime`               DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`               BIGINT              NULL,
    `modified_datetime`              DATETIME            NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_connecting_information_id`)
) ENGINE = InnoDB COMMENT ='회원 실명 인증 관리 테이블';

CREATE TABLE `user_social_account`
(
    `user_social_account_id` BIGINT AUTO_INCREMENT COMMENT '회원 소셜계정 순번 PK',
    `user_id`                BIGINT                             NOT NULL COMMENT '회원순번',
    `service_type`           VARCHAR(20)                        NOT NULL COMMENT '소셜서비스 유형',
    `service_id`             VARCHAR(150)                       NOT NULL COMMENT '소셜서비스 내 회원 고유 식별키',
    `delete_yn`              VARCHAR(1)                         NOT NULL,
    `created_user_id`        BIGINT                             NOT NULL,
    `created_datetime`       DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`       BIGINT                             NULL,
    `modified_datetime`      DATETIME                           NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_social_account_id`),
    CONSTRAINT `fk_user_social_account_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB COMMENT ='회원 소셜 계정 관리 테이블';

CREATE TABLE `user_delivery_addresses`
(
    `user_delivery_addresses_id` BIGINT AUTO_INCREMENT COMMENT '회원배송지 순번 PK',
    `user_id`           BIGINT                                 NOT NULL COMMENT '회원순번',
    `address_name`      VARCHAR(100)   DEFAULT                 NULL COMMENT '배송지 별칭',
    `first_name`        VARCHAR(100)   DEFAULT                 NULL COMMENT '수취인 이름',
    `last_name`         VARCHAR(100)                           NOT NULL COMMENT '수취인 성',
    `mobile_number`     VARCHAR(30)                            NOT NULL COMMENT '수취인 휴대폰번호',
    `tel_number`        VARCHAR(30)    DEFAULT                 NULL COMMENT '수취인 전화번호',
    `address1`          VARCHAR(255)   DEFAULT                 NULL COMMENT '주소',
    `address2`          VARCHAR(255)   DEFAULT                 NULL COMMENT '상세주소',
    `city`              VARCHAR(100)   DEFAULT                 NULL COMMENT '시',
    `state`             VARCHAR(100)   DEFAULT                 NULL COMMENT '도/주',
    `zipcode`           VARCHAR(12)    DEFAULT                 NULL COMMENT '우편번호',
    `delivery_message`  VARCHAR(255)   DEFAULT                 NULL COMMENT '배송메시지',
    `default_yn`        CHAR(1)        DEFAULT 'N'                  COMMENT '기본배송지 여부',
    `address_valid_yn`  char(1)        DEFAULT                 NULL COMMENT '주소검증 여부',
    `delete_yn`         VARCHAR(1)                             NOT NULL,
    `created_user_id`   BIGINT                                 NOT NULL,
    `created_datetime`  DATETIME       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`  BIGINT                                 NULL,
    `modified_datetime` DATETIME                               NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_delivery_addresses_id`),
    CONSTRAINT `fk_user_delivery_addresses_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB COMMENT ='회원 배송지 정보 관리 테이블';

CREATE TABLE `user_refund_bank_account`
(
    `user_refund_bank_account_id` BIGINT AUTO_INCREMENT                          COMMENT '환불계좌 순번 PK',
    `user_id`                     BIGINT      DEFAULT                       NULL COMMENT '회원순번',
    `bank_code`                   VARCHAR(10)                           NOT NULL COMMENT '은행코드',
    `bank_name`                   VARCHAR(25)                           NOT NULL COMMENT '은행명',
    `bank_account_no`             VARCHAR(200)                          NOT NULL COMMENT '계좌번호',
    `bank_holder_name`            VARCHAR(50)                           NOT NULL COMMENT '예금주',
    `connecting_information`      VARCHAR(88) DEFAULT                       NULL COMMENT '비회원 CI 값',
    `sales_order_number`          VARCHAR(50) DEFAULT                       NULL COMMENT '주문번호',
    `memo`                        TEXT                                           COMMENT '메모',
    `delete_yn`                   VARCHAR(1)                            NOT NULL,
    `created_user_id`             BIGINT                                NOT NULL,
    `created_datetime`            DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`            BIGINT                                NULL,
    `modified_datetime`           DATETIME                              NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_refund_bank_account_id`),
    CONSTRAINT `fk_user_refund_bank_account_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE = InnoDB COMMENT ='회원 환불계좌 관리 테이블';

CREATE TABLE `user_refund_bank_account_history`
(
    `user_refund_bank_account_history_id` BIGINT AUTO_INCREMENT COMMENT '환불계좌 이력 순번 PK',
    `user_refund_bank_account_id`         BIGINT           NOT NULL COMMENT '환불계좌 순번',
    `user_id`                             BIGINT       DEFAULT NULL COMMENT '회원순번',
    `connecting_information`              VARCHAR(88)  DEFAULT NULL COMMENT '비회원 CI 값',
    `sales_order_number`                  VARCHAR(50)  DEFAULT NULL COMMENT '주문번호',
    `before_bank_code`                    VARCHAR(10)  DEFAULT NULL COMMENT '변경전 은행코드',
    `before_bank_name`                    VARCHAR(25)  DEFAULT NULL COMMENT '변경전 은행명',
    `before_bank_account_no`              VARCHAR(200) DEFAULT NULL COMMENT '변경전 계좌번호',
    `before_bank_holder_name`             VARCHAR(50)  DEFAULT NULL COMMENT '변경전 예금주',
    `after_bank_code`                     VARCHAR(10)  DEFAULT NULL COMMENT '변경후 은행코드',
    `after_bank_name`                     VARCHAR(25)  DEFAULT NULL COMMENT '변경후 은행명',
    `after_bank_account_no`               VARCHAR(200) DEFAULT NULL COMMENT '변경후 계좌번호',
    `after_bank_holder_name`              VARCHAR(50)  DEFAULT NULL COMMENT '변경후 예금주',
    `memo`                                TEXT                      COMMENT '메모',
    `delete_yn`                           VARCHAR(1)                             NOT NULL,
    `created_user_id`                     BIGINT                                 NOT NULL,
    `created_datetime`                    DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`                    BIGINT                                 NULL,
    `modified_datetime`                   DATETIME                               NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_refund_bank_account_history_id`),
    CONSTRAINT `fk_user_refund_bank_account_history_parent` FOREIGN KEY (`user_refund_bank_account_id`)
        REFERENCES `user_refund_bank_account` (`user_refund_bank_account_id`),
    CONSTRAINT `fk_user_refund_bank_account_history_user_id` FOREIGN KEY (`user_id`)
        REFERENCES `user` (`user_id`)
) ENGINE = InnoDB COMMENT ='회원 환불계좌 이력 관리 테이블';

CREATE TABLE `user_information_change_history`
(
    `user_information_change_history_id` BIGINT AUTO_INCREMENT COMMENT '회원정보 변경이력 순번 PK',
    `user_id`                            BIGINT     NOT NULL COMMENT '회원순번',
    `log_type`                           VARCHAR(50)         DEFAULT NULL COMMENT '변경항목 유형 (대분류)',
    `log_sub_type`                       VARCHAR(50)         DEFAULT NULL COMMENT '변경항목 유형 (소분류)',
    `description`                        TEXT COMMENT '이력 설명',
    `note`                               TEXT COMMENT '이력 비고',
    `occurrence_datetime`                DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '정보변경일시',
    `before_change`                      VARCHAR(255)        DEFAULT NULL COMMENT '변경전',
    `after_change`                       VARCHAR(255)        DEFAULT NULL COMMENT '변경후',
    `delete_yn`                          VARCHAR(1) NOT NULL,
    `created_user_id`                    BIGINT     NOT NULL,
    `created_datetime`                   DATETIME            DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`                   BIGINT     NULL,
    `modified_datetime`                  DATETIME   NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_information_change_history_id`)
) ENGINE = InnoDB COMMENT ='회원 정보 변경 이력 관리 테이블';

CREATE TABLE `withdrawal_user`
(
    `withdrawal_user_id`  BIGINT AUTO_INCREMENT COMMENT '탈퇴회원 순번 PK',
    `user_id`             BIGINT                                 NOT NULL COMMENT '회원순번',
    `withdrawal_type`     VARCHAR(30)                            NOT NULL COMMENT '탈퇴사유 유형',
    `note`                TEXT                                            COMMENT '탈퇴 비고',
    `withdrawal_datetime` DATETIME                               NOT NULL COMMENT '탈퇴일시',
    `delete_yn`           VARCHAR(1)                             NOT NULL,
    `created_user_id`     BIGINT                                 NOT NULL,
    `created_datetime`    DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`    BIGINT                                 NULL,
    `modified_datetime`   DATETIME                               NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`withdrawal_user_id`),
    CONSTRAINT `fk_withdrawal_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB COMMENT ='탈퇴 회원 정보 관리 테이블';

-- ===========================================================================================================
-- 상품
-- ===========================================================================================================

CREATE TABLE `product`
(
    `product_id`        BIGINT AUTO_INCREMENT COMMENT '상품순번 PK',
    `product_code`      VARCHAR(20)                        NOT NULL COMMENT '상품코드',
    `product_name`      VARCHAR(150)                       NOT NULL COMMENT '상품명',
    `brand`             VARCHAR(100)                       NOT NULL COMMENT '상품브랜드',
    `product_type`      VARCHAR(100)                       NOT NULL COMMENT '상품유형',
    `delete_yn`         VARCHAR(1)                         NOT NULL,
    `created_user_id`   BIGINT                             NOT NULL,
    `created_datetime`  DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`  BIGINT                             NULL,
    `modified_datetime` DATETIME                           NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`product_id`)
) ENGINE = InnoDB COMMENT '상품 관리 테이블';

CREATE TABLE `category`
(
    `category_id`       BIGINT AUTO_INCREMENT COMMENT '카테고리 순번 PK',
    `upper_category_id` BIGINT   DEFAULT NULL COMMENT '상위 카테고리 순번',
    `category_type`     VARCHAR(20)                        NOT NULL COMMENT '카테고리 유형',
    `category_code`     VARCHAR(20)                        NOT NULL COMMENT '카테고리 코드',
    `category_name`     VARCHAR(50)                        NOT NULL COMMENT '카테고리 명',
    `delete_yn`         VARCHAR(1)                         NOT NULL,
    `created_user_id`   BIGINT                             NOT NULL,
    `created_datetime`  DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`  BIGINT                             NULL,
    `modified_datetime` DATETIME                           NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`category_id`)
) ENGINE = InnoDB COMMENT '카테고리 관리 테이블';

CREATE TABLE `tag`
(
    `tag_id`            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '태그순번 PK',
    `tag_name`          VARCHAR(50) NOT NULL COMMENT '태그명',
    `version`           INT         NOT NULL DEFAULT '1' COMMENT '버전',
    `delete_yn`         VARCHAR(1)  NOT NULL,
    `created_user_id`   BIGINT      NOT NULL,
    `created_datetime`  DATETIME             DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`  BIGINT      NULL,
    `modified_datetime` DATETIME    NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`tag_id`)
) ENGINE = InnoDB COMMENT ='태그 정보 관리 테이블';

CREATE TABLE `product_category`
(
    `product_category_id`        BIGINT     NOT NULL AUTO_INCREMENT COMMENT '상품 카테고리 순번 PK',
    `product_id`                 BIGINT     NOT NULL COMMENT '상품순번',
    `category_id`                BIGINT     NOT NULL COMMENT '카테고리 순번',
    `main_category_yn`           CHAR(1)    NOT NULL DEFAULT 'N' COMMENT '대표 카테고리 여부',
#     `display_order`              INT        NOT NULL DEFAULT '0' COMMENT '노출 순서',
#     `category_display_order`     INT        NOT NULL DEFAULT '0' COMMENT '카테고리 노출 순서',
    `version`                    INT        NOT NULL DEFAULT '1' COMMENT '버전',
    `delete_yn`                  VARCHAR(1) NOT NULL,
    `created_user_id`            BIGINT     NOT NULL,
    `created_datetime`           DATETIME            DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`           BIGINT     NULL,
    `modified_datetime`          DATETIME   NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`product_category_id`)
) ENGINE = InnoDB COMMENT ='상품 카테고리 관리 테이블';

CREATE TABLE `product_tag`
(
    `product_tag_id`    BIGINT     NOT NULL AUTO_INCREMENT COMMENT '상품 태그 순번 PK',
    `product_id`        BIGINT     NOT NULL COMMENT '상품 순번',
    `tag_id`            BIGINT     NOT NULL COMMENT '태그 순번',
    `version`           INT        NOT NULL DEFAULT '1' COMMENT '버전',
    `delete_yn`         VARCHAR(1) NOT NULL,
    `created_user_id`   BIGINT     NOT NULL,
    `created_datetime`  DATETIME            DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`  BIGINT     NULL,
    `modified_datetime` DATETIME   NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`product_tag_id`)
) ENGINE = InnoDB COMMENT ='상품 태그 관리 테이블';

CREATE TABLE `image`
(
    `image_id`                 BIGINT AUTO_INCREMENT COMMENT '이미지순번 PK',
    `product_id`               BIGINT                             NOT NULL COMMENT '상품순번',
    `admin_image_file_name`    VARCHAR(1000)                      NOT NULL COMMENT '이미지파일 관리 이름',
    `original_image_file_name` VARCHAR(1000)                      NOT NULL COMMENT '이미지파일 원본 이름',
    `image_file_name`          VARCHAR(1000)                      NOT NULL COMMENT '이미지파일 이름',
    `image_file_url`           TEXT                               NOT NULL COMMENT '이미지파일 url',
    `image_type`               VARCHAR(20)                        NOT NULL COMMENT '이미지유형',
#     `display_order`            INT                                NOT NULL COMMENT '노출순서',
    `content_type`             VARCHAR(20)                        NOT NULL COMMENT '컨텐츠유형',
    `width`                    INT                                NOT NULL COMMENT '너비',
    `height`                   INT                                NOT NULL COMMENT '높이',
    `file_size`                INT                                NOT NULL COMMENT '파일크기',
    `delete_yn`                VARCHAR(1)                         NOT NULL,
    `created_user_id`          BIGINT                             NOT NULL,
    `created_datetime`         DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`         BIGINT                             NULL,
    `modified_datetime`        DATETIME                           NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`image_id`)
) ENGINE = InnoDB COMMENT ='상품 이미지';

-- ===========================================================================================================
-- 주문
-- ===========================================================================================================

CREATE TABLE `cart`
(
    `cart_id`           BIGINT      NOT NULL COMMENT '장바구니 채번 PK',
    `user_id`           BIGINT      NOT NULL COMMENT '회원순번',
    `product_id`        BIGINT      NOT NULL COMMENT '상품채번',
    `product_option_id` BIGINT               DEFAULT NULL COMMENT '상품옵션 순번',
    `quantity`          BIGINT      NOT NULL COMMENT '수량',
    `cart_type`         VARCHAR(50) NOT NULL DEFAULT 'EMPTY_CART' COMMENT '장바구니유형',
    `uuid`              VARCHAR(100)         DEFAULT NULL COMMENT '게스트 식별자',
    `delete_yn`         VARCHAR(1)  NOT NULL,
    `created_user_id`   BIGINT      NOT NULL,
    `created_datetime`  DATETIME             DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`  BIGINT      NULL,
    `modified_datetime` DATETIME    NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`cart_id`)
) ENGINE = InnoDB COMMENT ='장바구니 관리 테이블';

CREATE TABLE `sales_order`
(
    `sales_order_id`       BIGINT                             NOT NULL COMMENT '주문채번 PK',
    `user_id`              BIGINT                             NOT NULL COMMENT '회원순번',
    `sales_order_number`   VARCHAR(50)                        NOT NULL COMMENT '주문번호',
    `sales_order_type`     VARCHAR(50)                        NOT NULL COMMENT '주문유형',
    `sales_order_status`   VARCHAR(50)                        NOT NULL COMMENT '주문상태',
    `order_datetime`       DATETIME                           NOT NULL COMMENT '주문일시',
    `completion_datetime`  DATETIME                           NULL COMMENT '주문완료 일시',
    `cancel_datetime`      DATETIME                           NULL COMMENT '주문취소 일시',
    `cancel_reason_type`   VARCHAR(50)                        NULL COMMENT '주문취소 유형',
    `order_aggregation_id` BIGINT                             NOT NULL COMMENT '주문집계 채번',
    `delete_yn`            VARCHAR(1)                         NOT NULL,
    `created_user_id`      BIGINT                             NOT NULL,
    `created_datetime`     DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`     BIGINT                             NULL,
    `modified_datetime`    DATETIME                           NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`sales_order_id`)
) ENGINE = InnoDB COMMENT '주문 관리 테이블';

CREATE TABLE `sales_order_history`
(
    `sales_order_history_id` BIGINT                                   NOT NULL COMMENT '주문이력 채번 PK',
    `sales_order_id`         BIGINT                                   NOT NULL COMMENT '주문채번',
    `payment_status`         VARCHAR(50)                              NOT NULL COMMENT '결제상태',
    `payment_type`           VARCHAR(50)    DEFAULT                   NULL COMMENT '결제유형',
    `products_amount`        DECIMAL(19, 2) DEFAULT                   NULL COMMENT '상품금액',
    `sale_amount`            DECIMAL(19, 2) DEFAULT                   NULL COMMENT '결제금액',
    `cancel_amount`          DECIMAL(19, 2) DEFAULT                   NULL COMMENT '취소금액',
    `refund_amount`          DECIMAL(19, 2) DEFAULT                   NULL COMMENT '환불금액',
    `delivery_fee_amount`    DECIMAL(19, 2) DEFAULT                   NULL COMMENT '배송금액',
    `delete_yn`              VARCHAR(1)                               NOT NULL,
    `created_user_id`        BIGINT                                   NOT NULL,
    `created_datetime`       DATETIME       DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`       BIGINT                                   NULL,
    `modified_datetime`      DATETIME                                 NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`sales_order_history_id`)
) ENGINE = InnoDB COMMENT ='주문 결제 이력 관리 테이블';

CREATE TABLE `return_order`
(
    `return_order_id`                  BIGINT                             NOT NULL COMMENT '반품채번 PK',
    `return_order_number`              VARCHAR(50)                        NOT NULL COMMENT '반품번호',
    `sales_order_id`                   BIGINT                             NOT NULL COMMENT '주문채번',
    `reception_datetime`               DATETIME                           NOT NULL COMMENT '접수일시',
    `reception_completion_datetime`    DATETIME     DEFAULT                   NULL COMMENT '접수완료 일시',
    `reception_cancel_datetime`        DATETIME     DEFAULT                   NULL COMMENT '접수취소 일시',
    `return_order_completion_datetime` DATETIME     DEFAULT                   NULL COMMENT '반품완료 일시',
    `return_order_cancel_datetime`     DATETIME     DEFAULT                   NULL COMMENT '반품취소 일시',
    `return_order_status`              VARCHAR(50)                        NOT NULL COMMENT '반품상태',
    `return_order_agree_yn`            VARCHAR(1)                         NOT NULL COMMENT '동의여부',
    `return_order_agree_datetime`      DATETIME     DEFAULT                   NULL COMMENT '동의일시',
    `reception_approval_yn`            VARCHAR(1)                         NOT NULL COMMENT '승인여부',
    `reception_approval_datetime`      DATETIME     DEFAULT                   NULL COMMENT '승인일시',
    `delete_yn`                        VARCHAR(1)                         NOT NULL,
    `created_user_id`                  BIGINT                             NOT NULL,
    `created_datetime`                 DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`                 BIGINT                             NULL,
    `modified_datetime`                DATETIME                           NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`return_order_id`)
) ENGINE = InnoDB COMMENT '반품 관리 테이블';

CREATE TABLE `return_order_history`
(
    `return_order_history_id`          BIGINT         NOT NULL COMMENT '반품이력 채번 PK',
    `return_order_id`                  BIGINT         NOT NULL COMMENT '반품채번',
    `return_order_number`              VARCHAR(50)    NOT NULL COMMENT '반품번호',
    `sales_order_id`                   BIGINT         NOT NULL COMMENT '주문채번',
    `reception_datetime`               DATETIME       NOT NULL COMMENT '접수일시',
    `reception_approval_datetime`      DATETIME       DEFAULT NULL COMMENT '접수승인 일시',
    `reception_completion_datetime`    DATETIME       DEFAULT NULL COMMENT '접수완료 일시',
    `reception_cancel_datetime`        DATETIME       DEFAULT NULL COMMENT '접수취소 일시',
    `return_order_completion_datetime` DATETIME       DEFAULT NULL COMMENT '반품완료 일시',
    `return_order_cancel_datetime`     DATETIME       DEFAULT NULL COMMENT '반품취소 일시',
    `return_order_status`              VARCHAR(50)    NOT NULL COMMENT '반품상태',
    `settlement_status`                VARCHAR(50)    NOT NULL COMMENT '정산상태',
    `payment_redemption_amount`        DECIMAL(19, 2) NOT NULL COMMENT '결제 환수금액',
    `return_order_agree_yn`            CHAR(1)        NOT NULL COMMENT '반품동의 여부',
    `return_order_agree_datetime`      DATETIME       DEFAULT NULL COMMENT '반품동의 일시',
    `reception_approval_yn`            CHAR(1)        NOT NULL COMMENT '접수승인 여부',
    `delivery_fee_amount`              DECIMAL(19, 2) DEFAULT NULL COMMENT '배송비',
    `return_shipping`                  DECIMAL(19, 2) DEFAULT NULL COMMENT '반품 배송비',
    `other_amount`                     DECIMAL(19, 2) DEFAULT NULL COMMENT '기타금액',
    `before_status`                    VARCHAR(100)   DEFAULT NULL COMMENT '이전 주문상태',
    `after_status`                     VARCHAR(100)   DEFAULT NULL COMMENT '이후 주문상태',
    `memo`                             VARCHAR(1000)  DEFAULT NULL COMMENT '메모',
    `user_name`                        VARCHAR(255)   DEFAULT NULL COMMENT '회원명',
    `product_id`                       BIGINT         DEFAULT NULL COMMENT '제품 식별자',
    `product_option_id`                BIGINT         DEFAULT NULL COMMENT '제품 옵션 채번',
    `quantity_before`                  BIGINT         DEFAULT NULL COMMENT '이전 수량',
    `quantity_after`                   BIGINT         DEFAULT NULL COMMENT '이후 수량',
    PRIMARY KEY (`return_order_history_id`)
) ENGINE = InnoDB COMMENT ='반품 이력 관리 테이블';

CREATE TABLE `refund`
(
    `refund_id`           BIGINT                                 NOT NULL COMMENT '환불채번 PK',
    `return_order_id`     BIGINT                                 NOT NULL COMMENT '반품채번',
    `refund_payment_type` VARCHAR(50)                            NOT NULL COMMENT '환불 결제유형',
    `refund_datetime`     DATETIME                               NOT NULL COMMENT '환불일시',
    `payment_amount`      BIGINT                                 NOT NULL COMMENT '결제금액',
    `adjustment_amount`   BIGINT                                 NOT NULL COMMENT '조정금액',
    `total_refund_amount` BIGINT                                 NOT NULL COMMENT '전체 환불금액',
    `adjustment_reason`   VARCHAR(200)      DEFAULT                  NULL COMMENT '조정사유',
    `bank_name`           VARCHAR(100)      DEFAULT                  NULL COMMENT '은행명',
    `account_no`          VARCHAR(100)      DEFAULT                  NULL COMMENT '계좌번호',
    `delete_yn`           VARCHAR(1)                             NOT NULL,
    `created_user_id`     BIGINT                                 NOT NULL,
    `created_datetime`    DATETIME          DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`    BIGINT                                 NULL,
    `modified_datetime`   DATETIME                               NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`refund_id`)
) ENGINE = InnoDB COMMENT '환불 관리 테이블';

CREATE TABLE `refund_history`
(
    `refund_history_id`   BIGINT                                  NOT NULL COMMENT '환불이력 채번 PK',
    `refund_id`           BIGINT                                  NOT NULL COMMENT '환불채번 PK',
    `return_order_id`     BIGINT                                  NOT NULL COMMENT '반품채번',
    `refund_payment_type` VARCHAR(50)                             NOT NULL COMMENT '환불 결제유형',
    `refund_datetime`     DATETIME                                NOT NULL COMMENT '환불일시',
    `payment_amount`      BIGINT                                  NOT NULL COMMENT '결제금액',
    `adjustment_amount`   BIGINT                                  NOT NULL COMMENT '조정금액',
    `total_refund_amount` BIGINT                                  NOT NULL COMMENT '전체 환불금액',
    `adjustment_reason`   VARCHAR(200)      DEFAULT                   NULL COMMENT '조정사유',
    `bank_name`           VARCHAR(100)      DEFAULT                   NULL COMMENT '은행명',
    `account_no`          VARCHAR(100)      DEFAULT                   NULL COMMENT '계좌번호',
    `delete_yn`           VARCHAR(1)                                  NOT NULL,
    `created_user_id`     BIGINT                                      NOT NULL,
    `created_datetime`    DATETIME          DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`    BIGINT                                     NULL,
    `modified_datetime`   DATETIME                                   NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`refund_history_id`)
) ENGINE = InnoDB COMMENT '환불이력 관리 테이블';

-- ===========================================================================================================
-- 결제
-- ===========================================================================================================

CREATE TABLE `payment`
(
    `payment_id`        BIGINT                                NOT NULL COMMENT '결제채번 PK',
    `domain_type`       VARCHAR(50)                           NOT NULL COMMENT '도메인 유형',
    `order_id`          BIGINT                                NOT NULL COMMENT '주문채번',
    `order_number`      VARCHAR(20)                           NOT NULL COMMENT '주문번호',
    `user_id`           BIGINT                                NOT NULL COMMENT '회원순번',
    `user_type`         VARCHAR(50)                           NOT NULL COMMENT '회원유형',
    `payment_type`      VARCHAR(50)                           NOT NULL COMMENT '결제유형',
    `payment_status`    VARCHAR(50)                           NOT NULL COMMENT '결제상태',
    `amount`            BIGINT                                NOT NULL COMMENT '결제금액',
    `cancel_amount`     BIGINT                                NOT NULL COMMENT '취소금액',
    `pg_order_number`   VARCHAR(50) DEFAULT                       NULL COMMENT 'PG 주문번호',
    `pg_approve_number` VARCHAR(50) DEFAULT                       NULL COMMENT 'PG 승인번호',
    `request_date`      DATE                                  NOT NULL COMMENT '요청일',
    `complete_date`     DATE        DEFAULT                       NULL COMMENT '완료일',
    `cancel_date`       DATE        DEFAULT                       NULL COMMENT '취소일',
    `receipt`           VARCHAR(200)                          NOT NULL COMMENT '영수증',
    `delete_yn`         VARCHAR(1)                            NOT NULL,
    `created_user_id`   BIGINT                                NOT NULL,
    `created_datetime`  DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`  BIGINT                                NULL,
    `modified_datetime` DATETIME                              NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`payment_id`)
) ENGINE = InnoDB COMMENT '결제 관리 테이블';

CREATE TABLE `payment_history`
(
    `payment_history_id`   BIGINT                             NOT NULL COMMENT '결제이력 채번 PK',
    `payment_id`           BIGINT                             NOT NULL COMMENT '결제채번',
    `payment_request_type` VARCHAR(50)                        NOT NULL COMMENT '요청유형',
    `amount`               BIGINT                             NOT NULL COMMENT '결제금액',
    `request_datetime`     DATETIME                           NOT NULL COMMENT '요청일시',
    `delete_yn`            VARCHAR(1)                         NOT NULL,
    `created_user_id`      BIGINT                             NOT NULL,
    `created_datetime`     DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`     BIGINT                             NULL,
    `modified_datetime`    DATETIME                           NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`payment_history_id`)
) ENGINE = InnoDB COMMENT '결제이력 관리 테이블';

CREATE TABLE `payment_cancel`
(
    `payment_cancel_id`           BIGINT                                  NOT NULL COMMENT '결제취소 채번 PK',
    `payment_id`                  BIGINT                                  NOT NULL COMMENT '결제채번',
    `cancel_status`               VARCHAR(100)                            NOT NULL COMMENT '결제취소 상태',
    `cancel_amount`               DECIMAL(19, 2)                          NOT NULL COMMENT '결제취소 금액',
    `sales_order_id`              BIGINT                                  NOT NULL COMMENT '주문채번',
    `order_number`                VARCHAR(100)  DEFAULT                       NULL COMMENT '주문번호',
    `cancel_service_type`         VARCHAR(100)  DEFAULT                       NULL COMMENT '결제취소 유형',
    `cancel_request_time`         DATETIME                                NOT NULL COMMENT '결제취소 요청일시',
    `cancel_complete_time`        DATETIME      DEFAULT                       NULL COMMENT '결제취소 완료일시',
    `cancel_reason`               VARCHAR(1000) DEFAULT                       NULL COMMENT '결제취소 사유',
    `cancel_transaction_key`      VARCHAR(100)  DEFAULT                       NULL COMMENT '결제취소 거래키',
    `user_refund_bank_account_id` BIGINT        DEFAULT                       NULL COMMENT '결제 환불계좌 아이디',
    `delete_yn`                   VARCHAR(1)                              NOT NULL,
    `created_user_id`             BIGINT                                  NOT NULL,
    `created_datetime`            DATETIME      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`            BIGINT                                  NULL,
    `modified_datetime`           DATETIME                                NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`payment_cancel_id`),
    CONSTRAINT `fk_payment_cancel_payment_id` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`payment_id`) ON DELETE CASCADE
) ENGINE = InnoDB COMMENT ='결제취소 관리 테이블';

CREATE TABLE `refund_accounts`
(
    `refund_accounts_id`          BIGINT AUTO_INCREMENT                           COMMENT '환불 계좌정보 순번 PK',
    `sales_order_id`              BIGINT                                 NOT NULL COMMENT '주문채번',
    `order_number`                VARCHAR(50)  DEFAULT                       NULL COMMENT '주문번호',
    `user_id`                     BIGINT       DEFAULT                       NULL COMMENT '회원순번',
    `user_refund_bank_account_id` BIGINT       DEFAULT                       NULL COMMENT '회원 환불계좌 순번',
    `bank_name`                   VARCHAR(100) DEFAULT                       NULL COMMENT '은행명',
    `account_no`                  VARCHAR(100) DEFAULT                       NULL COMMENT '계좌번호',
    `identification`              VARCHAR(100) DEFAULT                       NULL COMMENT '비회원 식별자',
    `delete_yn`                   VARCHAR(1)                             NOT NULL,
    `created_user_id`             BIGINT                                 NOT NULL,
    `created_datetime`            DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`            BIGINT                                 NULL,
    `modified_datetime`           DATETIME                               NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`refund_accounts_id`)
) ENGINE = InnoDB COMMENT ='환불 계좌정보 관리 테이블';

-- ===========================================================================================================
-- 정산
-- ===========================================================================================================

CREATE TABLE `order_close`
(
    `order_close_id`      BIGINT                                NOT NULL COMMENT '주문마감 채번 PK',
    `user_id`             BIGINT                                NOT NULL COMMENT '회원순번',
    `user_type`           VARCHAR(50)                           NOT NULL COMMENT '회원유형',
    `order_type`          VARCHAR(50)                           NOT NULL COMMENT '주문유형',
    `settle_datetime`     DATETIME                              NOT NULL COMMENT '정산일시',
    `settle_month`        VARCHAR(10) DEFAULT                       NULL COMMENT '정산월',
    `base_date`           DATE        DEFAULT                       NULL COMMENT '정산 기준일',
    `sales_order_id`      BIGINT                                NOT NULL COMMENT '주문채번',
    `return_order_id`     BIGINT      DEFAULT                       NULL COMMENT '반품채번',
    `product_sale_amount` DECIMAL(19, 2)                        NOT NULL COMMENT '조정 상품가 합',
    `close_month`         VARCHAR(10) DEFAULT                       NULL COMMENT '마감월',
    `delete_yn`           VARCHAR(1)                            NOT NULL,
    `created_user_id`     BIGINT                                NOT NULL,
    `created_datetime`    DATETIME    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`    BIGINT                                NULL,
    `modified_datetime`   DATETIME                              NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`order_close_id`)
) ENGINE = InnoDB COMMENT ='주문 마감 관리 테이블';

CREATE TABLE `settle_base_daily`
(
    `settle_base_daily_id` BIGINT                             NOT NULL COMMENT '일정산 기초 정보 채번 PK',
    `settle_month`         VARCHAR(6)                         NOT NULL COMMENT '정산월',
    `base_date`            DATE                               NOT NULL COMMENT '기준일',
    `user_id`              BIGINT                             NOT NULL COMMENT '회원순번',
    `daily_amount`         DECIMAL(19, 2)                     NOT NULL COMMENT '일 구매금액',
    `delete_yn`            VARCHAR(1)                         NOT NULL,
    `created_user_id`      BIGINT                             NOT NULL,
    `created_datetime`     DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    `modified_user_id`     BIGINT                             NULL,
    `modified_datetime`    DATETIME                           NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`settle_base_daily_id`)
) ENGINE = InnoDB COMMENT ='일정산 기초정보 관리 테이블';




package com.aebong.store.common.util;

import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

public class DefaultDateTimeFormat {

    public static final DateTimeFormatter DATE_FORMAT = ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMAT = ofPattern("HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_FORMAT = ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_FORMAT_KR = ofPattern("yyyy년MM월dd일 HH:mm:ss");

    public static final DateTimeFormatter DATE_TIME_FORMAT_LANDMARK = ofPattern("MM/dd/yyyy HH:mm a");

    public static final DateTimeFormatter DATE_TIME_MILLISECOND_FORMAT = ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static final DateTimeFormatter DATE_NONE_DASH_FORMAT = ofPattern("yyyyMMdd");

    public static final DateTimeFormatter DATE_TIME_NONE_DASH_FORMAT = ofPattern("yyyyMMddHHmmss");

    public static final DateTimeFormatter DEFAULT_UPLOAD_FILE_NAME_TIMESTAMP_FORMAT = ofPattern("yyyyMMddHHmmn");

    public static final DateTimeFormatter YEAR_MONTH_NONE_DASH_FORMAT = ofPattern("yyyyMM");
    public static final DateTimeFormatter FRONT_TWO_YEAR_MONTH_NONE_DASH_FORMAT = ofPattern("yyMM");
    public static final DateTimeFormatter DATE_TIME_RESIDENT_REG_NO_FORMAT = ofPattern("yyMMdd");

    public static final DateTimeFormatter YEAR_MONTH_FORMAT = ofPattern("yyyy-MM");
}

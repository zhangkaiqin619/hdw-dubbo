package com.hdw.common.exception;

import com.hdw.common.api.IErrorCode;

/**
 * @Description 断言处理类，用于抛出各种API异常
 * @Author TuMingLong
 * @Date 2020/3/28 16:41
 */
public class Asserts {
    public static void fail(String message) {
        throw new GlobalException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new GlobalException(errorCode);
    }
}

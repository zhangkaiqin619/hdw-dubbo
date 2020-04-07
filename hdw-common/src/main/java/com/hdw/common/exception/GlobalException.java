package com.hdw.common.exception;

import com.hdw.common.api.IErrorCode;

/**
 * @Description 自定义异常
 * @Author TuMingLong
 * @Date 2018/12/10 13:59
 */

public class GlobalException extends RuntimeException {
    private IErrorCode errorCode;

    public IErrorCode getErrorCode() {
        return errorCode;
    }

    public GlobalException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(Throwable cause) {
        super(cause);
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }

}

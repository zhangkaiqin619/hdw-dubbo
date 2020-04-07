package com.hdw.exception;

import com.hdw.common.api.CommonResult;
import com.hdw.common.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * @Description 全局异常处理
 * @Author TuMingLong
 * @Date 2020/3/28 16:41
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    public CommonResult handle(GlobalException e) {
        if (e.getErrorCode() != null) {
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException: {}", e.getBindingResult().getFieldError().getDefaultMessage());
        return CommonResult.failed(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public CommonResult handleValidationException(ValidationException e) {
        log.error("handleValidationException: {}", e.getCause().getMessage());
        return CommonResult.failed(e.getCause().getMessage());
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult handleConstraintViolationException(ConstraintViolationException e) {
        log.error("handleConstraintViolationException: {}", e.getMessage());
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 登录异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AuthorizationException.class)
    public CommonResult handleAuthorizationException(AuthorizationException e) {
        log.error("handleAuthorizationException: {}", e.getMessage());
        return CommonResult.unauthorized(e.getMessage());
    }

}

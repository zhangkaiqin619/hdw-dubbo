package com.hdw.common.constants;

/**
 * @Description 公共常量
 * @Author TuMinglong
 * @Date 2019/5/10 13:59
 **/
public class CommonConstants {
    /**
     * 自定义错误
     */
    public static final String X_ERROR = "x.servlet.exception.code";
    public static final String X_ERROR_CODE = "x.servlet.exception.error";
    public static final String X_ERROR_MESSAGE = "x.servlet.exception.message";
    public static final String X_ACCESS_DENIED = "x.access.denied";

    /** 超级管理员ID */
    public static final int SUPER_ADMIN = 1;

    /**
     * 客户端ID KEY
     */
    public static final String SIGN_CLIENT_ID_KEY = "clientId";

    /**
     * 客户端秘钥 KEY
     */
    public static final String SIGN_CLIENT_SECRET_KEY = "clientSecret";

    /**
     * 随机字符串 KEY
     */
    public static final String SIGN_NONCE_KEY = "nonce";
    /**
     * 时间戳 KEY
     */
    public static final String SIGN_TIMESTAMP_KEY = "timestamp";
    /**
     * 签名类型 KEY
     */
    public static final String SIGN_SIGN_TYPE_KEY = "signType";
    /**
     * 签名结果 KEY
     */
    public static final String SIGN_SIGN_KEY = "sign";
}

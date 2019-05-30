package com.hdw.common.constants;

/**
 * @Description 菜单枚举
 * @Author TuMinglong
 * @Date 2018/12/13 18:37
 */
public enum MenuEnum {

    YES(0, "是"),
    NO(1, "不是"),//不是菜单的是按钮
    OPEN(0, "开启"),
    CLOSE(1, "关闭");

    int code;
    String message;

    MenuEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String valueOf(Integer code) {
        if (code == null) {
            return "";
        } else {
            for (MenuEnum s : values()) {
                if (s.getCode() == code) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}

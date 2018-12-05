package com.etalk.crm.utils;

/**
 * @author Terwer Rest响应客户端的状态值
 */
public enum RestResponseStates {

    SERVER_ERROR(0, "系统忙，请稍候再试。如有疑问请联系管理员"),

    SUCCESS(1, "请求成功，无任何异常");

    private Integer value;
    private String msg;

    private RestResponseStates(Integer value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

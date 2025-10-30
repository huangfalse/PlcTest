package com.mes.result;

import lombok.Getter;

/**
 * 统一的结果码枚举类，定义系统中常见的响应状态码与对应的消息。
 * 用于封装接口返回值中的状态表示，便于前后端统一处理。
 *
 * code：状态码
 * message：提示信息
 */
/**
 * ResultCodeEnum class
 *
 * @author zsh
 * @date 2025/07/04
 */
@Getter
public enum ResultCodeEnum {

    /**
     * 操作成功
     */
    SUCCESS(200, "成功"),

    /**
     * 操作失败
     */
    FAIL(201, "失败"),

    /**
     * 服务内部异常
     */
    SERVICE_ERROR(2012, "服务异常"),

    /**
     * 数据错误，例如空数据、数据格式异常等
     */
    DATA_ERROR(204, "数据异常"),

    /**
     * 非法请求，可能存在参数篡改、非法访问等
     */
    ILLEGAL_REQUEST(205, "非法请求"),

    /**
     * 表示用户重复提交了相同请求（如表单）
     */
    REPEAT_SUBMIT(206, "重复提交"),

    /**
     * 用户未登录或登录信息失效
     */
    LOGIN_AUTH(208, "未登录"),

    /**
     * 无操作权限
     */
    PERMISSION(209, "没有权限");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态描述信息
     */
    private final String message;

    /**
     * 构造方法
     *
     * @param code    状态码
     * @param message 状态信息
     */
    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取状态消息
     *
     * @return 状态消息
     */
    public String getMessage() {
        return message;
    }
}

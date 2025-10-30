package com.mes.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Result class
 *
 * @author zsh
 * @date 2025/07/04
 */
@Data
public class Result<T> {
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private T data;

    private Result() {

    }

    public static <T> Result<T> build(Integer code, String message, T data) {
        Result<T> result = new Result<T>();
        result.setCode(code);
        result.setMessage(message);
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> Result<T> build(ResultCodeEnum resultCodeEnum, T data) {
        Result<T> result = new Result<T>();
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> Result<T> success() {
        return build(ResultCodeEnum.SUCCESS, null);
    }

    public static <T> Result<T> success(T data) {
        return build(ResultCodeEnum.SUCCESS.getCode(), "", data);
    }


    public static Result error(Integer code, String msg) {
        return build(code, msg, null);
    }

    public static <T> Result<T> error(T data) {
        return build(ResultCodeEnum.FAIL.getCode(), "", data);
    }

    public static <T> Result<T> error() {
        return build(ResultCodeEnum.FAIL.getCode(), "服务器发生异常", null);
    }
}

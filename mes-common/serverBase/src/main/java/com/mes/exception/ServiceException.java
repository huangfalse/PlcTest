package com.mes.exception;

import com.mes.result.ResultCodeEnum;
import lombok.Getter;
/**
 * ServiceException class
 *
 * @author zsh
 * @date 2025/07/04
 */
@Getter
public class ServiceException extends RuntimeException {
    private Integer code;

    public ServiceException(ResultCodeEnum resultCodeEnum, String msg) {
        super(msg);
        this.code = resultCodeEnum.getCode();
    }
}

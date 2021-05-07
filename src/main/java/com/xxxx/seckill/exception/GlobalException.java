package com.xxxx.seckill.exception;

import com.xxxx.seckill.constant.BaseResInterface;
import lombok.Data;

/**
 * 自定义全局异常
 */
@Data
public class GlobalException extends RuntimeException{
    protected String errorCode;
    protected String errorMsg;

    public GlobalException(BaseResInterface baseResInterface){
        this.errorCode = baseResInterface.getResultCode();
        this.errorMsg = baseResInterface.getResultMsg();
    }

    public GlobalException(String errorCode,String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}

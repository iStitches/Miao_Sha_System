package com.xxxx.seckill.exception;

import com.xxxx.seckill.constant.CommonEnum;
import com.xxxx.seckill.constant.ResultObj;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //处理自定义全局异常
    @ExceptionHandler(value = GlobalException.class)
    public ResultObj dealWithGlobalException(GlobalException e){
        log.error("出现异常，详细信息为："+e.getMessage());
        return ResultObj.error(e.getErrorCode(),e.getErrorMsg());
    }

    //处理参数绑定格式不匹配问题
    @ExceptionHandler(value = BindException.class)
    public ResultObj dealWithValidatorException(BindException e){
        String msg = e.getAllErrors().get(0).getDefaultMessage();
        log.error("参数绑定出现异常："+msg);
        return ResultObj.error(CommonEnum.WORD_BIND_ERROR.getResultCode(),msg);
    }

    @ExceptionHandler(value = Exception.class)
    public ResultObj dealWithOthers(Exception e){
        String msg = e.getMessage();
        log.error("其余异常："+msg);
        e.printStackTrace();
        return ResultObj.error(CommonEnum.ERROR.getResultCode(),msg);
    }

}

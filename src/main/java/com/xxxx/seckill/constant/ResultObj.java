package com.xxxx.seckill.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultObj implements Serializable {
    private String code;
    private String msg;
    private Object obj;

    //成功
    public static ResultObj success(){
        return new ResultObj(CommonEnum.SUCCESS.getResultCode(), CommonEnum.SUCCESS.getResultMsg(),null);
    }

    public static ResultObj success(Object obj){
        return new ResultObj(CommonEnum.SUCCESS.getResultCode(),CommonEnum.SUCCESS.getResultMsg(),obj);
    }
    public static ResultObj success(String code,String msg,Object obj){
        if(obj != null)
            return new ResultObj(code,msg,obj);
        else
            return new ResultObj(code,msg,null);
    }


    //失败
    public static ResultObj error(BaseResInterface baseResInterface){
        return new ResultObj(baseResInterface.getResultCode(),baseResInterface.getResultMsg(),null);
    }
    public static ResultObj error(){
        return new ResultObj(CommonEnum.ERROR.getResultCode(),CommonEnum.ERROR.getResultMsg(),null);
    }
    public static ResultObj error(String code,String msg){
        return new ResultObj(code,msg,null);
    }
}

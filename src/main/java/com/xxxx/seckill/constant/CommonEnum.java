package com.xxxx.seckill.constant;

public enum CommonEnum implements BaseResInterface{
    //通用
    SUCCESS("200","SUCCESS"),
    ERROR("500","服务器内部错误"),
    //登录
    ACCOUNT_NOT_FOUND("500200","用户不存在，请注册"),
    PASSWORD_FALSE("500201","密码错误请重试"),
    WORD_BIND_ERROR("500202","参数绑定异常"),
    ACCOUNT_ALREADY_EXISTS("500203","用户已经存在，不能重复注册"),
    ACCOUNT_VERIFY_FAIL("500204","用户token认证失败，无权访问！"),
    //秒杀
    GOODS_STACK_ZERO("500210","库存为0，抢购失败"),
    GOODS_ALREADY_ROB("500211","您已经抢购过该商品，不能重复抢购"),
    QUEUE_SEND_DEFAULT("500212","消息队列发送消息失败"),
    MIAO_SHA_ERROR("500213","商品秒杀已结束")
    ;

    CommonEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private String errorCode;
    private String errorMsg;

    @Override
    public String getResultCode() {
        return errorCode;
    }

    @Override
    public String getResultMsg() {
        return errorMsg;
    }
}

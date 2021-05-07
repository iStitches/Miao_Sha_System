package com.xxxx.seckill.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.TYPE})
@Documented
@Constraint(
        validatedBy = {isMobileValidator.class}
)
public @interface isMobile {
    boolean required() default false;

    String message() default "手机格式不符合标准，请修改后重新输入";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

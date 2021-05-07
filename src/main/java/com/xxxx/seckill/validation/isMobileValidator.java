package com.xxxx.seckill.validation;


import com.xxxx.seckill.util.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class isMobileValidator implements ConstraintValidator<isMobile, Long> {
    private boolean isRequired;

    @Override
    public void initialize(isMobile constraintAnnotation) {
        isRequired = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(Long s, ConstraintValidatorContext constraintValidatorContext) {
        if(isRequired){
            return ValidatorUtil.isMobile(s);
        }
        else
            if(s == null)
                return true;
            return ValidatorUtil.isMobile(s);
    }
}

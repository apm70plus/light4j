package com.light.kaptcha.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.light.kaptcha.KaptchaProperties;

/**
 * 校验码验证注解。
 *
 * @author liuyg
 * @version 1.0
 */
@Target({ElementType.TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {SmsKaptchaValidator.class })
public @interface SmsKaptcha {
    String message() default "验证码错误";
    
    String scope() default KaptchaProperties.DEFAULT_SCOPE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

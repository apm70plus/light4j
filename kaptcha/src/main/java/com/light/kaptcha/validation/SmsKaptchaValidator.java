package com.light.kaptcha.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.light.kaptcha.KaptchaService;

public class SmsKaptchaValidator implements ConstraintValidator<SmsKaptcha, SmsCaptchaGetter> {

	private String scope;
	
    @Override
    public void initialize(final SmsKaptcha constraintAnnotation) {
    	scope = constraintAnnotation.scope();
    }
	
	@Override
	public boolean isValid(SmsCaptchaGetter value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value.getCaptcha()) || StringUtils.isBlank(value.getMobile())) {
            return false;
        }
        return KaptchaService.instance().validCaptcha(value.getMobile(), scope, value.getCaptcha());
	}
}

package com.light.kaptcha.validation;

import javax.servlet.http.Cookie;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.light.kaptcha.KaptchaService;

/**
 * 校验码验证器实现类。
 *
 * @author liuyg
 * @version 1.0
 */
public class KaptchaValidator implements ConstraintValidator<Kaptcha, String> {

	private String scope;
	
    @Override
    public void initialize(final Kaptcha constraintAnnotation) {
    	scope = constraintAnnotation.scope();
    }

    /*
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
     * javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }
        // 从Session获取校验码，并清空Session中的校验码，避免多次校验
        return KaptchaService.instance().validCaptcha(getCaptchaKey(), scope, value);
    }
    
    private String getCaptchaKey() {
    	ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		for(Cookie cookie : attributes.getRequest().getCookies()) {
			if ("CAPTCHAID".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
}

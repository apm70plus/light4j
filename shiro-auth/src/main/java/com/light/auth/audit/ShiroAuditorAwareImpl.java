package com.light.auth.audit;

import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.light.auth.util.LoginUserUtils;

@Component("auditorAware")
@ConditionalOnClass(AuditorAware.class)
@ConditionalOnMissingBean(value=AuditorAware.class)
public class ShiroAuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.ofNullable(LoginUserUtils.getLoginUserId());
	}

}

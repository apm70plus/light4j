package com.light.oauth2.config;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

import com.light.oauth2.authc.OAuth2Token;

public class ShiroSubjectFactory extends DefaultWebSubjectFactory {

	@Override
	public Subject createSubject(SubjectContext context) {
		AuthenticationToken token = context.getAuthenticationToken();
		if (token instanceof OAuth2Token) {
			// 当token为OAuth2Token时， 不创建 session
			context.setSessionCreationEnabled(false);
		}
		return super.createSubject(context);
	}
}

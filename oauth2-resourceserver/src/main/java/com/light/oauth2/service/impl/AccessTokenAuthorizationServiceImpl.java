package com.light.oauth2.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.light.oauth2.Oauth2Constants;
import com.light.oauth2.bean.LoginUser;
import com.light.oauth2.cache.Oauth2UserPermissionsCache;
import com.light.oauth2.service.AuthorizationService;

public class AccessTokenAuthorizationServiceImpl implements AuthorizationService {

	@Autowired
	private Oauth2UserPermissionsCache oauth2UserPermissionsCache;
	
	@Override
	public Collection<String> getAuthorizationInfo(LoginUser loginUser) {
		String oauth2Scope = loginUser.getExtension() != null ? (String)loginUser.getExtension().get("scope") : null;
		if (StringUtils.isEmpty(oauth2Scope)) {
			return Collections.emptyList();
		} else if (Oauth2Constants.USER_SELF_SCOPE.equals(oauth2Scope)) {
			return oauth2UserPermissionsCache.get(loginUser.getId());
		} else {
			return Arrays.asList(oauth2Scope.split(","));
		}
	}

}

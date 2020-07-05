package com.light.oauth2.repository;

import com.light.oauth2.model.AuthorizationCode;

public interface AuthorizationCodeRepository {

	AuthorizationCode findByCode(String code);

	void deleteByCode(String code);

	void save(AuthorizationCode code, long timeout);
}

package com.daliu.sample.service;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.light.auth.bean.LoginUser;
import com.light.auth.bean.UsernamePasswordToken;
import com.light.auth.service.AuthenticationService;

/**
 * 用户名密码，身份人证服务实现
 * @author daliu
 *
 */
@Service
public class AuthenticationServiceUsernamePasswdImpl implements AuthenticationService {

	@Autowired
	private LoginRetryCounter loginRetryCounter;
	
	@Override
	public LoginUser getAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username = token.getUsername();
		int leftRetryTimes = loginRetryCounter.leftRetryTimes(username);
		if (leftRetryTimes <= 0) {
			throw new AuthenticationException("已超出登录重试次数，请 " + loginRetryCounter.lockMinutes + " 分钟后再试") ;
		}
		
		String password = token.getPassword();
		if ("admin".equals(username) && "123456".equals(password)) {
			// 重置失败计数
			loginRetryCounter.resetFailureTimes(username);
			LoginUser user = new LoginUser();
			user.setName("admin");
			user.setId("1");
			user.setAdmin(true);
			return user;
		} else {
			// 失败计数
			loginRetryCounter.incrementFailureTimes(username);
			if (--leftRetryTimes <= 0) {
				throw new AuthenticationException("已超出登录重试次数，请 " + loginRetryCounter.lockMinutes + " 分钟后再试") ;
			} else {
				throw new AuthenticationException("登录失败，您还有" + leftRetryTimes + "次登录机会 ");
			}
		}
	}

	@Override
	public Class<? extends AuthenticationToken> supportTokenClass() {
		return UsernamePasswordToken.class;
	}

}

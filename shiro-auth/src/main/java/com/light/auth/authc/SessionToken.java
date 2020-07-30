package com.light.auth.authc;

import com.light.auth.bean.LoginUser;
import org.apache.shiro.authc.AuthenticationToken;

import javax.servlet.http.HttpSession;

public class SessionToken implements AuthenticationToken {

    public static final String PRINCIPAL_KEY = "shiro:loginUser";
    private LoginUser loginUserFromSession;
    private String principal;

    public SessionToken(HttpSession session) {
        this.loginUserFromSession = (LoginUser) session.getAttribute(PRINCIPAL_KEY);
        this.principal = session.getId();
    }

    public LoginUser getLoginUserFromSession() {
        return this.loginUserFromSession;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return this.principal;
    }
}

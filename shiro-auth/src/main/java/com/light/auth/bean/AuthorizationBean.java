package com.light.auth.bean;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;

@Getter
public class AuthorizationBean {

	private Collection<String> permissions = new ArrayList<>();
	
	private Collection<String> roles = new ArrayList<>();
	
	public void addPermissions(Collection<String> permissions) {
		this.permissions.addAll(permissions);
	}
	
	public void addRoles(Collection<String> roles) {
		this.roles.addAll(roles);
	}
}

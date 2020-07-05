package com.light.rbac.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.light.jpa.domain.AuditEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
@Table(indexes = {@Index(unique=true, name="login_id_IDX", columnList="loginId")})
public class SysUser extends AuditEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 姓名
	 */
    @Length(max = 32)
    @Column(length = 32)
	private String name;
	
    /**
     * 登录名称
     */
    @Length(max = 32)
    @Column(unique = true, length = 32)
    private String loginId;

    /**
     * 手机号码
     */
    @Length(max = 20)
    @Column(unique = true, length = 20)
    private String mobile;

    /**
     * 电子邮箱
     */
    @Length(max = 255)
    @Column(unique = true, length = 125)
    private String email;

    /**
     * 登录密码
     */
    @Length(max = 80)
    @Column(length = 80)
    private String password;
    
    /**
     * 帐号是否启用
     */
    private boolean enabled = true;

    /**
     * 帐号是否锁定
     */
    private boolean accountLocked = false;

    /**
     * 帐号是否过期
     */
    private boolean accountExpired = false;

    /**
     * 密码是否过期
     */
    private boolean credentialsExpired = false;
}

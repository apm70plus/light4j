package com.light.oauth2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.light.jpa.domain.AuditEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
@Table(indexes = {@Index(unique=true, name="client_id_IDX", columnList="clientId")})
public class Oauth2Client extends AuditEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 客户端名称
	 */
	@NotBlank
	@Size(max = 100)
	@Column(length = 100, nullable = false)
	private String clientName;
	
	/**
	 * 客户端ID
	 */
	@NotBlank
	@Size(max = 100)
	@Column(length = 100, nullable = false)
	private String clientId;
	
	/**
	 * 客户端安全key
	 */
	@NotBlank
	@Size(max = 100)
	@Column(length = 100, nullable = false)
	private String clientSecret;
	
	/**
	 * 客户端权限范围（逗号分割）
	 */
	@Size(max = 1024)
	@Column(length = 1024)
	private String scope;
	
	/**
	 * 资源服务名（内部服务，允许执行密码授权）
	 */
	@Size(max = 100)
	@Column(length = 100)
	private String resourceServer;
}

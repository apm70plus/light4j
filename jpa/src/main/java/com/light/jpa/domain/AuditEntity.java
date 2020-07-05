package com.light.jpa.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 领域模型基类（带审计功能）
 */
@JsonInclude(content=Include.NON_NULL)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditEntity extends BasicEntity implements Auditable<String, Long, LocalDateTime> {

	private static final long serialVersionUID = 1L;

	@JsonProperty
	@CreatedBy
	protected String createdBy;
	
	@JsonProperty
	@CreatedDate
	protected LocalDateTime createdDate;

	@JsonProperty
	@LastModifiedBy
	protected String lastModifiedBy;

	@JsonProperty
	@LastModifiedDate
	private LocalDateTime lastModifiedTime;

	@Override
	@JsonIgnore
	public Optional<String> getCreatedBy() {
		return Optional.ofNullable(this.createdBy);
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	@JsonIgnore
	public Optional<LocalDateTime> getCreatedDate() {
	    return Optional.ofNullable(createdDate);
	}

	public void setCreatedDate(final LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	@JsonIgnore
	public Optional<String> getLastModifiedBy() {
		return Optional.ofNullable(this.lastModifiedBy);
	}

	public void setLastModifiedBy(final String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	public LocalDateTime getLastModifiedTime() {
		return this.lastModifiedTime;
	}

	@Override
	@JsonIgnore
	public Optional<LocalDateTime> getLastModifiedDate() {
		return Optional.ofNullable(lastModifiedTime);
	}

	public void setLastModifiedDate(final LocalDateTime lastModifiedDate) {
		this.lastModifiedTime = lastModifiedDate;
	}
}

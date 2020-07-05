package com.light.jpa.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
public abstract class AssignIdAuditEntity<PK extends Serializable> implements Auditable<String, PK, LocalDateTime> {

    /** ID */
    @Id
    @Column(length = 36, nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
    protected PK id;
    
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
	protected LocalDateTime lastModifiedTime;

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Persistable#getId()
     */
    @Override
    public PK getId() {
        return this.id;
    }

    /**
     * Sets the id of the entity.
     *
     * @param id the id to set
     */
    public void setId(final PK id) {
        this.id = id;
    }

    /**
     * Must be {@link Transient} in order to ensure that no JPA provider
     * complains because of a missing setter.
     *
     * @see DATAJPA-622
     * @see org.springframework.data.domain.Persistable#isNew()
     */
    @Override
    @Transient
    public boolean isNew() {
        return null == this.getId();
    }
    
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

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), this.getId());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }

        final Persistable<?> that = (Persistable<?>) obj;

        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += null == this.getId() ? 0 : this.getId().hashCode() * 31;

        return hashCode;
    }
}

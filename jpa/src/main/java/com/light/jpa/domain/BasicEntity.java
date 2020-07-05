package com.light.jpa.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 领域模型基类（不带审计字段）
 * <p>
 */
@MappedSuperclass
@JsonInclude(content=Include.NON_NULL)
public abstract class BasicEntity implements Persistable<Long>, Serializable {

	private static final long serialVersionUID = 1L;
	// TODO 需要实现集群化的ID生成机制
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
	@Version
	@JsonIgnore
	protected long version;

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Persistable#getId()
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * Sets the id of the entity.
     *
     * @param id the id to set
     */
    protected void setId(final Long id) {
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
    @JsonIgnore
    public boolean isNew() {
        return null == this.getId();
    }
    
	public long getVersion() {
		return this.version;
	}

	public void setVersion(final long version) {
		this.version = version;
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

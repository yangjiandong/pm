package org.ssh.pm.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 资源类型
 */
@Entity
@Table(name = "t_resource_type")
public class ResourceType {
    private Long oid;
    private String typeName;
    private String note;

    @Id
    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    @Column(name = "TYPE_NAME",length=50)
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Column(length=200)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Transient
    public boolean isTransient() {
        return this.oid == null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

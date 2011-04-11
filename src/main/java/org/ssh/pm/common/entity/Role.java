package org.ssh.pm.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 角色.
 *
 */
@Entity
@Table(name = "T_ROLES")
public class Role extends IdEntity {

    private String name;
    private String desc;

    @Column(nullable = false, unique = true, length=20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length=100)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

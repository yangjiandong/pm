package org.ssh.pm.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.ssh.pm.orm.hibernate.AuditableEntity;

/**
 * 角色.
 *
 */
@Entity
@Table(name = "T_ROLES")
public class Role extends IdEntity {

    private String name;
    private String desc;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Id_Generator")
    @TableGenerator(name = "Id_Generator", table = "ID_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "T_ROLES", initialValue = 1, allocationSize = 1)
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

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

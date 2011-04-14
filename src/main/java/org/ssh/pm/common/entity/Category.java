package org.ssh.pm.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.ssh.pm.orm.hibernate.AuditableEntity;
import org.ssh.pm.orm.hibernate.Historizable;


/**
 * 商品分类
 *
 */
@Entity
@Table(name = "DEMO_CATEGORYS")
public class Category extends AuditableEntity implements Serializable,
    Historizable {
    private static final long serialVersionUID = 4527491559230071972L;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Id_Generator")
    @TableGenerator(name = "Id_Generator", table = "ID_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "T_CATEGORYS", initialValue = 100, allocationSize = 1)
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category[" + id + "," + name + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Category other = (Category) obj;

        if ((this.id != other.id)
                && ((this.id == null) || !this.id.equals(other.id))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;

        return hash = (47 * hash) + id.intValue();
    }
}

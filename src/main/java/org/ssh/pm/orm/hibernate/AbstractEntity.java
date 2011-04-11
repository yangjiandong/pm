package org.ssh.pm.orm.hibernate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 实体类的抽象基类，定义了id字段
 * @author jeffrey
 */
@MappedSuperclass
public abstract class AbstractEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
package org.ssh.pm.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 编号产生表
 */
@Entity
@Table(name = "T_GETNOS")
public class GetNo implements Serializable {
    private static final long serialVersionUID = -3189670041741755451L;

    private String noName;

    private Long noValue;

    private String note;

    @Id
    @Column(length = 50)
    public String getNoName() {
        return noName;
    }

    public void setNoName(String noName) {
        this.noName = noName;
    }

    public Long getNoValue() {
        return noValue;
    }

    public void setNoValue(Long noValue) {
        this.noValue = noValue;
    }

    @Column(length = 200, name = "NOTE")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

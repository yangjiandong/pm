package org.ssh.pm.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "T_PARTDB")
public class PartDB implements Serializable{
    private static final long serialVersionUID = -4471066536960096119L;

    private String dbcode;
    private String dbname;

    @Id
    @Column(length = 10, nullable = false)
    public String getDbcode() {
        return dbcode;
    }

    public void setDbcode(String dbcode) {
        this.dbcode = dbcode;
    }

    @Column(length = 10, nullable = false, unique = true)
    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

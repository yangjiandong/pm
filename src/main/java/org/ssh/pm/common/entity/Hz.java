package org.ssh.pm.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "T_HZK")
public class Hz {
    private Long oid;

    private String hz;
    private String wb;
    private String py;

    @Column(length = 10)
    public String getHz() {
        return hz;
    }

    public void setHz(String hz) {
        this.hz = hz;
    }

    @Column(length = 10)
    public String getWb() {
        return wb;
    }

    public void setWb(String wb) {
        this.wb = wb;
    }

    @Column(length = 10)
    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Id
    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }
}

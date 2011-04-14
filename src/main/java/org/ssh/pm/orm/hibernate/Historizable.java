package org.ssh.pm.orm.hibernate;

/**
 * Mark性质的接口，实现该接口的实体类表示需要审计日志
 * @author jeffrey
 */
public interface Historizable {
    public Long getId();
}

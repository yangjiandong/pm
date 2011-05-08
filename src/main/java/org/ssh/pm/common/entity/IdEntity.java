package org.ssh.pm.common.entity;

import org.springside.modules.orm.grid.ViewField;

/**
 * 2011.04.13
 * 改由UIdEntity实现原有功能,这里只实现getId
 */

public abstract class IdEntity {
    @ViewField()
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

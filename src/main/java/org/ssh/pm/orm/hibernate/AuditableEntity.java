package org.ssh.pm.orm.hibernate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springside.modules.orm.grid.ViewField;
import org.ssh.pm.common.entity.IdEntity;

/**
 * 含审计信息的Entity基类.
 *
 * @author calvin
 */
@MappedSuperclass
public class AuditableEntity extends IdEntity {

    @ViewField(header = "创建时间", align = "right")
    protected String createTime;
    @ViewField(header = "创建人")
    protected String createBy;
    @ViewField(header = "更新时间", align = "right")
    protected String lastModifyTime;
    @ViewField(header = "更新人")
    protected String lastModifyBy;

    /**
     * 创建时间.
     */
    //本属性只在save时有效,update时无效.
    @Column(updatable = false, name = "addedOn", length = 50)
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 创建的操作员的登录名.
     */
    @Column(updatable = false, name = "addedWho", length = 50)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 最后修改时间.
     */
    //本属性只在update时有效,save时无效.
    @Column(insertable = false, name = "updatedOn", length = 50)
    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    /**
     * 最后修改的操作员的登录名.
     */
    @Column(insertable = false, name = "updatedWho", length = 50)
    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }
}
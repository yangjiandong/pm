package org.ssh.pm.common.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springside.modules.orm.grid.ViewField;

import com.google.common.collect.Lists;

/**
 * 角色.
 *
 */
@Entity
@Table(name = "T_ROLES")
public class Role extends IdEntity {
    @ViewField(header = "角色名称")
    private String name;
    @ViewField(header = "描述")
    private String desc;

    //有序的关联对象集合
    //private List<Resource> resList = Lists.newArrayList();

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

    @Column(nullable = false, unique = true, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = 100)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

//	//多对多定义
//	@ManyToMany
//	//中间表定义,表名采用默认命名规则
//	@JoinTable(name = "T_ROLE_RES", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "RES_ID") })
//	//Fecth策略定义
//	@Fetch(FetchMode.SUBSELECT)
//	//集合按id排序
//	@OrderBy("id ASC")
//	public List<Resource> getResList() {
//		return resList;
//	}
//
//	public void setResList(List<Resource> resList) {
//		this.resList = resList;
//	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Transient
    public boolean isTransient() {
        return this.id == null;
    }
}

package org.ssh.pm.common.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.entity.RoleResource;

/**
 * 角色-资源对象的泛型Hibernate Dao.
 *
 * @author calvin
 */
@Component
public class RoleResourceDao extends HibernateDao<RoleResource, Long> {
}

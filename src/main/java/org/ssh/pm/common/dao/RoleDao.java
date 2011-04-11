package org.ssh.pm.common.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.entity.Role;

/**
 * 角色对象的泛型Hibernate Dao.
 *
 * @author calvin
 */
@Component
public class RoleDao extends HibernateDao<Role, String> {
}

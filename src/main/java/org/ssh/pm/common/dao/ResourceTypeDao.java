package org.ssh.pm.common.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.entity.ResourceType;

@Repository("resourceTypeDao")
public class ResourceTypeDao extends HibernateDao<ResourceType, Long> {

}

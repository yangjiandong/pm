package org.ssh.pm.common.dao;

import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.entity.Resource;

@Repository("resourcesDao")
public class ResourceDao extends HibernateDao<Resource, Long> {
    
}

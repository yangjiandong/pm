package org.ssh.pm.common.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.entity.Employee;

@Component
public class EmployeeDao extends HibernateDao<Employee, Integer> {
}

package org.ssh.pm.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.ssh.pm.common.dao.EmployeeDao;
import org.ssh.pm.common.entity.Employee;

@Component
//默认将类中的所有函数纳入事务管理.
@Transactional
public class EmployeeService {
    private static Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeDao employeeDao;

    public void initData() {
        Employee e = new Employee();
        e.setEmpName("test");
        e.setEmpNo("09");
        e.setEmpSalary(234.4D);
        this.employeeDao.save(e);

    }

}

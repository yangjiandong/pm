package org.ssh.pm.common.service;

import java.util.List;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.ServiceException;
import org.ssh.pm.common.dao.RoleDao;
import org.ssh.pm.common.dao.RoleResourceDao;
import org.ssh.pm.common.entity.Role;

@Service
@Transactional
public class RoleService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleResourceDao roleResourceDao;

    /**
     * 获取全部角色
     */
    @Transactional(readOnly = true)
    public Page<Role> getAllRoles(Page<Role> page, List<PropertyFilter> filters) {
        Page<Role> curPage = roleDao.findPage(page, filters);
        List<Role> list = curPage.getResult();
        curPage.setResult(list);
        return curPage;
    }

    /**
     * 保存角色
     */
    public void saveRole(Role role) throws ServiceException {
        Role o = null;
        Session session = this.roleDao.getSession();
        try {
            session.beginTransaction();
            if (role.isTransient()) {
                o = roleDao.findUniqueBy("name", role.getName());
            } else {
                o = roleDao.findUnique("from Role where name = ? and id <> ? ", role.getName(), role.getId());
            }
            if (o != null) {
                throw new Exception();
            }
            roleDao.save(role);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            if (o == null) {
                logger.error("保存角色失败：", e);
                throw new ServiceException("保存角色失败");
            } else {
                throw new ServiceException("此角色名已存在，不能重复添加!");
            }
        }
    }


    /**
     * 删除角色
     */
    public void delete(Long roleId) throws ServiceException {
        Session session = this.roleDao.getSession();
        try {
            session.beginTransaction();
            Role role = roleDao.findUniqueBy("id", roleId);
            if (role != null) {
                if (role.getName().equals("admin")) {
                    logger.warn("操作员{}尝试删除超级管理员用户角色", "当前用户");
                    throw new Exception("不能删除超级管理员角色");
                }
                roleDao.delete(role);
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("删除角色失败:" + e.getMessage());
            throw new ServiceException();
        }
    }
}

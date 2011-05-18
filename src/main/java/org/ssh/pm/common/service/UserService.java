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
import org.ssh.pm.common.dao.UserDao;
import org.ssh.pm.common.entity.User;

@Service("userService")
@Transactional
public class UserService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserDao userDao;

    /**
     * 获取全部用户
     */
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Page<User> page, List<PropertyFilter> filters) {
        Page<User> curPage = userDao.findPage(page, filters);
        List<User> list = curPage.getResult();
        curPage.setResult(list);
        return curPage;
    }

    /**
     * 保存用户
     */
    public void saveUser(User user) throws ServiceException {
        User o = null;
        Session session = this.userDao.getSession();
        try {
            session.beginTransaction();
            if (user.isTransient()) {
                o = userDao.findUniqueBy("name", user.getName());
            } else {
                o = userDao.findUnique("from Role where name = ? and id <> ? ", user.getName(), user.getId());
            }
            if (o != null) {
                throw new Exception();
            }
            userDao.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            if (o == null) {
                logger.error("保存用户失败：", e);
                throw new ServiceException("保存用户失败");
            } else {
                throw new ServiceException("此用户名已存在，不能重复添加!");
            }
        }
    }


    /**
     * 删除用户
     */
    public void delete(Long userId) throws ServiceException {
        Session session = this.userDao.getSession();
        try {
            session.beginTransaction();
            User user = userDao.findUniqueBy("id", userId);
            if (user != null) {
                if (user.getName().equals("管理员")) {
                    logger.warn("操作员{}尝试删除超级管理员用户", "当前用户");
                    throw new Exception("不能删除超级管理员");
                }
                userDao.delete(user);
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("删除用户失败:" + e.getMessage());
            throw new ServiceException();
        }
    }
}

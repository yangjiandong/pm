package org.ssh.pm.common.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.entity.User;

/**
 * 用户对象的泛型Hibernate Dao.
 *
 * @author calvin
 */
@Component
public class UserDao extends HibernateDao<User, String> {

    private static final String QUERY_USER_WITH_ROLE = "select u from User u left join fetch u.roleList order by u.id";
    private static final String COUNT_USERS = "select count(u) from User u";
    private static final String DISABLE_USERS = "update User u set u.status='disabled' where id in(:ids)";

    /**
     * 批量修改用户状态.
     */
    public void disableUsers(List<String> ids) {
        Map<String, List<String>> map = Collections.singletonMap("ids", ids);
        batchExecute(UserDao.DISABLE_USERS, map);
    }

    /**
     * 使用 HQL 预加载lazy init的List<Role>,用DISTINCE_ROOT_ENTITY排除重复数据.
     */
    @SuppressWarnings("unchecked")
    public List<User> getAllUserWithRoleByDistinctHql() {
        Query query = createQuery(QUERY_USER_WITH_ROLE);
        return distinct(query).list();
    }

    /**
     * 使用Criteria 预加载lazy init的List<Role>, 用DISTINCE_ROOT_ENTITY排除重复数据.
     */
    @SuppressWarnings("unchecked")
    public List<User> getAllUserWithRolesByDistinctCriteria() {
        Criteria criteria = createCriteria().setFetchMode("roleList", FetchMode.JOIN);
        return distinct(criteria).list();
    }

    /**
     * 统计用户数.
     */
    public Long getUserCount() {
        return findUnique(UserDao.COUNT_USERS);
    }

    /**
     * 初始化User的延迟加载关联roleList.
     */
    public void initUser(User user) {
        initProxyObject(user.getRoleList());
    }
}

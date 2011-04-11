package org.ssh.pm.common.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.entity.Resource;

@Repository("resourcesDao")
public class ResourceDao extends HibernateDao<Resource, Long> {
    /**
     * 根据查询HQL与参数列表创建Query对象.
     *
     * @param values 命名参数,按名称绑定.
     */
    public Query createQueryByCache(final String queryString, final Map<String, ?> values) {
        Assert.hasText(queryString, "queryString不能为空");
        Query query = getSession().createQuery(queryString);
        //query.setCacheable(true);
        if (values != null) {
            query.setProperties(values);
        }
        return query;
    }

    /**
     * 根据查询HQL与参数列表创建Query对象.
     *
     * 本类封装的find()函数全部默认返回对象类型为T,当不为T时使用本函数.
     *
     * @param values 数量可变的参数,按顺序绑定.
     */
    public Query createQueryByCache(final String queryString, final Object... values) {
        Assert.hasText(queryString, "queryString不能为空");
        Query query = getSession().createQuery(queryString);
        //query.setCacheable(true);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    /**
     * 按HQL查询对象列表.
     *
     * @param values 命名参数,按名称绑定.
     */
    public <X> List<X> findByCache(final String hql, final Map<String, ?> values) {
        return createQueryByCache(hql, values).list();
    }

    /**
     * 按HQL查询对象列表.
     *
     * @param values 数量可变的参数,按顺序绑定.
     */
    public <X> List<X> findByCache(final String hql, final Object... values) {
        return createQueryByCache(hql, values).list();
    }
}

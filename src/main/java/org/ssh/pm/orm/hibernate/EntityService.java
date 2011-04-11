package org.ssh.pm.orm.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.orm.PropertyFilter.MatchType;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.springside.modules.utils.ServiceException;

/**
 * Service层领域对象业务管理类基类.
 * 使用HibernateDao<T,PK>进行业务对象的操作,子类需重载getEntityDao()函数提供该DAO.
 *
 * @param <T>
 *            领域对象类型
 * @param <PK>
 *            领域对象的主键类型
 *
 */
@Transactional
public abstract class EntityService<T, PK extends Serializable> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 在子类实现此函数,为下面的操作提供DAO.
     */
    protected abstract HibernateDao<T, PK> getEntityDao();

    public void delete(final T entity) throws ServiceException {
        getEntityDao().delete(entity);
    }

    public void delete(final PK id) throws ServiceException {
        getEntityDao().delete(id);
    }

    @Transactional(readOnly = true)
    public T get(final PK id) {
        return getEntityDao().get(id);
    }

    @Transactional(readOnly = true)
    public List<T> getAll() {
        return getEntityDao().getAll();
    }

    @Transactional(readOnly = true)
    public List<T> findBy(final String propertyName, final Object value) {
        return getEntityDao().findBy(propertyName, value);
    }

    @Transactional(readOnly = true)
    public T findByUnique(final String propertyName, final Object value) {
        return getEntityDao().findUniqueBy(propertyName, value);
    }

    @Transactional(readOnly = true)
    public List<T> find(final String hql, final Object... values) {
        return getEntityDao().find(hql, values);
    }

    @Transactional(readOnly = true)
    public List<T> find(final String hql, final Map<String, Object> values) {
        return getEntityDao().find(hql, values);
    }

//    @Transactional(readOnly = true)
//    public T findUnique(final String hql, final Object... values) {
//        return getEntityDao().findUnique(hql, values);
//    }

    public int batchExecute(final String hql, final Object... values) {
        return getEntityDao().batchExecute(hql, values);
    }

    public int batchExecute(final String hql, final Map<String, Object> values) {
        return getEntityDao().batchExecute(hql, values);
    }

    public Query createQuery(final String queryString, final Object... values) {
        return getEntityDao().createQuery(queryString, values);
    }

    public Query createQuery(final String queryString, final Map<String, Object> values) {
        return getEntityDao().createQuery(queryString, values);
    }

    @Transactional(readOnly = true)
    public List<T> find(final Criterion... criterions) {
        return getEntityDao().find(criterions);
    }

    @Transactional(readOnly = true)
    public List<T> findBy(final String propertyName, final Object value, final MatchType matchType) {
        return getEntityDao().findBy(propertyName, value, matchType);
    }

    @Transactional(readOnly = true)
    public T findUnique(final Criterion... criterions) {
        return getEntityDao().findUnique(criterions);
    }

    @Transactional(readOnly = true)
    public Page<T> getAll(final Page<T> page) {
        return getEntityDao().getAll(page);
    }

    @Transactional(readOnly = true)
    public List<T> find(List<PropertyFilter> filters) {
        return getEntityDao().find(filters);
    }

    @Transactional(readOnly = true)
    public Page<T> search(final Page<T> page, final List<PropertyFilter> filters) {
        return getEntityDao().findPage(page, filters);
    }

    @Transactional(readOnly = true)
    public Page<T> search(final Page<T> page, final String hql, final Object... values) {
        return getEntityDao().findPage(page, hql, values);
    }

    public boolean isPropertyUnique(final String propertyName, final Object newValue,
            final Object oldValue) {
        return getEntityDao().isPropertyUnique(propertyName, newValue, oldValue);
    }

}
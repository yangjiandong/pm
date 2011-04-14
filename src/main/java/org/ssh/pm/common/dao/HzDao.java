package org.ssh.pm.common.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.entity.Hz;

@Repository("hzDao")
public class HzDao extends HibernateDao<Hz, Long> {
    private static final String COUNTS = "select count(hz) from Hz u";
    final int batchSize = 20;//same as the JDBC batch size

    public Long getHzCount() {
        return findUnique(COUNTS);
    }

    public int batchCreate(final List<Hz> entityList) {
        // in the DAO
        //Session session = getSession();
        //Transaction tx = session.beginTransaction();

        int insertedCount = 0;
        for (int i = 0; i < entityList.size(); ++i) {
            save(entityList.get(i));
            if (++insertedCount % batchSize == 0) {
                flushAndClear();
                //session.flush();
                //session.clear();
            }
        }
        flushAndClear();

        //tx.commit();
        //session.clear();
        return insertedCount;
    }

    protected void flushAndClear() {
        if (getSession().isDirty()) {
            getSession().flush();
            getSession().clear();
        }
    }

    //返回第一条
    public Hz findOne(String one) {
        Criteria criteria = getSession().createCriteria(entityClass);
        return (Hz)criteria.add( Restrictions.eq("hz", one)).setMaxResults(1).uniqueResult();
    }
}

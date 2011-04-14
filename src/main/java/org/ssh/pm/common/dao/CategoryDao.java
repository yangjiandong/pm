package org.ssh.pm.common.dao;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;
import org.ssh.pm.common.entity.Category;

@Component
public class CategoryDao extends HibernateDao<Category, Long> {
    private static final String COUNTS = "select count(b) from " +Category.class.getName() + " b";

    /**
     * count .
     */
    public Long getCategoryCount() {
        return findUnique(COUNTS);
    }
}

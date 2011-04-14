package org.ssh.pm.common.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.ssh.pm.common.dao.CategoryDao;
import org.ssh.pm.common.entity.Category;

@Component
//默认将类中的所有函数纳入事务管理.
@Transactional
public class CategoryService {
    private static Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryDao categoryDao;

    @Transactional(readOnly = true)
    public List<Category> getBooks3(){
        return categoryDao.getAll();
    }

    public void initData() {
        if (this.categoryDao.getCategoryCount().longValue() != 0) {
            return;
        }

        Category b = new Category();
        b.setName("yang");
        categoryDao.save(b);

        b = new Category();
        b.setName("杨建东,和你");
        categoryDao.save(b);
    }
}

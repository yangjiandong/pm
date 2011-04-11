package org.ssh.pm.orm.hibernate;

import org.hibernate.Hibernate;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;

/**
 * 演示扩展H2Dialect,增加两种新函数.
 *
 * @author calvin
 */
public class H2ExtDialect extends H2Dialect {

    public H2ExtDialect() {
        super();
        //新函数up(), 等同于uppper()的缩写, 如where up(u.name)="FOO".
        registerFunction("up", new StandardSQLFunction("upper"));
        //新函数sample(),用于按某个百分比随机决定是否返回该条数据, 如where sample()>50  按50%概率返回数据.
        registerFunction("sample", new SQLFunctionTemplate(Hibernate.DOUBLE, "rand()*100", true));
    }
}

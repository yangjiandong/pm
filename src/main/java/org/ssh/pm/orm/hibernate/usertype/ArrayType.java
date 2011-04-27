package org.ssh.pm.orm.hibernate.usertype;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

//import com.opensymphony.oscache.util.StringUtil;

// 自定义数据
// <property name="emails" column="emails" type="com.lyl.hibernate.mytype.ArrayType"/>
// http://sudalyl.iteye.com/blog/1019787
//
public class ArrayType implements UserType {

    private static final char Spliter = ';';

    /**
     * 自定义类型的完全复制方法,构造返回对象
     *    1. 当nullSafeGet方法调用之后，我们获得了自定义数据对象，在向用户返回自定义数据之前
     * deepCopy方法被调用，它将根据自定义数据对象构造一个完全拷贝，把拷贝返还给客户使用。
     *    2.此时我们就得到了自定义数据对象的两个版本
     *     原始版本由hibernate维护，用作脏数据检查依据，复制版本由用户使用，hibernate将在
     * 脏数据检查过程中比较这两个版本的数据。
     *
     *
     */
    @Override
    public Object deepCopy(Object value) throws HibernateException {
        List source = (List) value;
        List target = new ArrayList();
        target.addAll(source);
        return target;
    }

    /**
     * 自定义数据类型比对方法
     * 用作脏数据检查，X,Y为两个副本
     */
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x != null && y != null) {
            List xList = (List) x;
            List ylList = (List) y;
            if (xList.size() != ylList.size()) {
                return false;
            }
            for (int i = 0; i < xList.size(); i++) {
                String s1 = (String) xList.get(i);
                String s2 = (String) ylList.get(i);
                if (!s1.equals(s2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 返回给定类型的hashCode
     */
    @Override
    public int hashCode(Object value) throws HibernateException {
        return value.hashCode();
    }

    /**
     * 表示本类型实例是否可变
     */
    @Override
    public boolean isMutable() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * 读取数据转换为自定义类型返回
     * names包含了自定义类型的映射字段名称
     */
    @SuppressWarnings("deprecation")
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        //取得字段名称并查询
        String value = (String) Hibernate.STRING.nullSafeGet(rs, names[0]);
        if (value != null) {
            return parse(value);
        } else {
            return null;
        }

    }

    /**
     * 数据保存时被调用
     */
    @Override
    public void nullSafeSet(PreparedStatement ps, Object value, int index) throws HibernateException, SQLException {
        if (value != null) {
            String str = combain((List) value);
            //保存数据
            Hibernate.STRING.nullSafeSet(ps, str, index);
        } else {
            //空值就直接保存了
            Hibernate.STRING.nullSafeSet(ps, value.toString(), index);
        }
    }

    /**
     * 将数据解析为LIST返回
     * @param value
     * @return
     */
    private List parse(String value) {
        String[] strs = org.apache.commons.lang.StringUtils.split(value, Spliter);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < strs.length; i++) {
            list.add(strs[i]);
        }

        //List list=StringUtils.split(value, Spliter);
        return list;
    }

    private String combain(List list) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size() - 1; i++) {
            sb.append(list.get(i)).append(Spliter);
        }
        sb.append(list.get(list.size() - 1));
        return sb.toString();
    }

    /**
     * 修改类型对应的java类型
     * 我们这边使用LIST类型
     */
    @Override
    public Class returnedClass() {
        return List.class;
    }

    /**
     * 修改类型对应的SQL类型
     * 使用VARCHAR
     */
    @Override
    public int[] sqlTypes() {
        return new int[] { Types.VARCHAR };
    }

    @Override
    public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
        return null;
    }

    /**
     * 不知干嘛用的
     */
    @Override
    public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
        return null;
    }

    @Override
    public Serializable disassemble(Object arg0) throws HibernateException {
        return null;
    }
}

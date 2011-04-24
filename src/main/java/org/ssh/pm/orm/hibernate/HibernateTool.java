package org.ssh.pm.orm.hibernate;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

public class HibernateTool {
    /**
     * <pre>
     * 获取实体所有字段
     * @param clazz 实体类型
     * @param strs 需要排除的字段
     * @return Map<String, Method> 其中key为数据库字段名称，value为字段对应的get方法
     * @author <a href="mailto:youliang.li86@feinno.com">OnTheWay</a>
     * 2011-4-23 下午01:52:06
     * </pre>
     */
    public static Map<String, Method> getFields(Class<?> clazz, List<String> strs) {
        Map<String, Method> map = new HashMap<String, Method>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Column c = method.getAnnotation(Column.class);
            if (null!=strs&&strs.contains(c.name())) {
                continue;
            }
            if (null != c) {
                map.put(c.name(), method);
            } else {
                JoinColumn jc = method.getAnnotation(JoinColumn.class);
                if (null!=strs&&strs.contains(jc.name())) {
                    continue;
                }
                if (null != jc) {
                    map.put(jc.name(), method);
                }
            }
        }
        return map;
    }
}

package org.ssh.pm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//http://hfkiss44.iteye.com/blog/568525
//List retultList=resultSetToList(resultset);
//遍历retultList 将list里面的Map都转换成pojo
//Student stu=(Student)BeanUtils.MapToBean(new Student(),(Map)retultList.get(0));
public class ReportUtils {

    public static List resultSetToList(ResultSet rs) throws java.sql.SQLException {
        if (rs == null) {
            return null;
        }
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        List list = new ArrayList();
        Map rowData;
        while (rs.next()) {
            rowData = new HashMap(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    public static Object MapToBean(Object bean, Map map) {
        Map methods = new HashMap();
        Method m[] = bean.getClass().getMethods();
        for (int i = 0; i < m.length; i++) {
            Method method = m[i];
            String methodName = method.getName().toUpperCase();
            methods.put(methodName, method);
        }
        Iterator it = null;
        String key = "";
        it = map.keySet().iterator();
        while (it.hasNext()) {
            key = (String) it.next();
            String name = "GET" + key.toUpperCase();
            if (methods.containsKey(name)) {
                Method setMethod = (Method) methods.get("SET" + key.toUpperCase());
                try {
                    if (setMethod != null) {
                        Object[] obj = null;
                        obj = new Object[1];
                        obj[0] = map.get(key);
                        setMethod.invoke(bean, obj);
                    } else {
                        continue;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

}

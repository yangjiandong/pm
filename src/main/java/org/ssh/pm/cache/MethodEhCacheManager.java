package org.ssh.pm.cache;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.springside.modules.utils.spring.SpringContextHolder;

public class MethodEhCacheManager {
    private static Cache methodCache = (Cache) SpringContextHolder.getBean("methodCache");
    private static Logger logger = Logger.getLogger(MethodEhCacheManager.class);

    private static boolean isStop = true;//初始化暂停

    public synchronized static boolean isStop() {
        return isStop;
    }

    public synchronized static void setStop(boolean isStop) {
        MethodEhCacheManager.isStop = isStop;
    }

    public synchronized static void put(Object key, Object value) {
        if (!isStop()) {
            Element e = new Element(key, value);
            logger.debug("cache object to methodCache ... key:" + key + ",size of value��" + e.getSerializedSize());
            methodCache.put(e);
        }
    }

    public synchronized static Object get(Object key) {
        if (isStop())
            return null;
        Element element = methodCache.get(key);
        if (element != null)
            logger.debug("get object from methodCache... key:" + key);
        return element == null ? null : element.getObjectValue();
    }

    public synchronized static void remove(Object key) {
        logger.debug("remove object from methodCache... key:" + key);
        methodCache.remove(key);
    }

    public synchronized static void removeAll() {
        logger.debug("remove all object from methodCache");
        methodCache.removeAll();
    }

    public synchronized static List getKeys() {
        return methodCache.getKeys();
    }

    public synchronized static int getSize() {
        return methodCache.getSize();
    }

    public synchronized static String getCacheName() {
        return methodCache.getName();
    }

    public synchronized static String getCacheKey(Class _class, String methodName, Object... arguments) {
        StringBuffer sb = new StringBuffer("methodCache_");
        sb.append(_class.getName()).append(".").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                sb.append(".").append(arguments[i]);
            }
        }
        return sb.toString();
    }

}

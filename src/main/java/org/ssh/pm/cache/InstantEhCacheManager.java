package org.ssh.pm.cache;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.springside.modules.utils.spring.SpringContextHolder;

public class InstantEhCacheManager {
    private static Cache instantCache = (Cache) SpringContextHolder.getBean("instantCache");
    private static Logger logger = Logger.getLogger(InstantEhCacheManager.class);

    private static boolean isStop = true;//初始化停止

    public synchronized static boolean isStop() {
        return isStop;
    }

    public synchronized static void setStop(boolean isStop) {
        InstantEhCacheManager.isStop = isStop;
    }

    public synchronized static void put(Object key, Object value) {
        if (!isStop()) {
            Element e = new Element(key, value);
            logger.debug("cache object to instantCache... key:" + key + ",size of value��" + e.getSerializedSize());
            instantCache.put(e);
        }
    }

    public synchronized static Object get(Object key) {
        if (isStop())
            return null;
        Element element = instantCache.get(key);
        if (element != null)
            logger.debug("get object from instantCache... key:" + key);
        return element == null ? null : element.getObjectValue();
    }

    public synchronized static void remove(Object key) {
        logger.debug("remove object from instantCache... key:" + key);
        instantCache.remove(key);
    }

    public synchronized static void removeAll() {
        logger.debug("remove all object from instantCache");
        instantCache.removeAll();
    }

    public synchronized static List getKeys() {
        return instantCache.getKeys();
    }

    public synchronized static int getSize() {
        return instantCache.getSize();
    }

    public synchronized static String getCacheName() {
        return instantCache.getName();
    }

    public synchronized static String getCacheKey(Class _class, String methodName, Object... arguments) {
        StringBuffer sb = new StringBuffer("instantCache_");
        sb.append(_class.getName()).append(".").append(methodName);
        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                sb.append(".").append(arguments[i]);
            }
        }
        return sb.toString();
    }
}

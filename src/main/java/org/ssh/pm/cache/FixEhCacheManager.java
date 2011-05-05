package org.ssh.pm.cache;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.springside.modules.utils.spring.SpringContextHolder;


public class FixEhCacheManager {
    private static Cache fixCache = (Cache) SpringContextHolder.getBean(
            "fixCache");
    private static Logger logger = Logger.getLogger(FixEhCacheManager.class);
    private static boolean isStop = true; //初始化暂停

    public synchronized static boolean isStop() {
        return isStop;
    }

    public synchronized static void setStop(boolean isStop) {
        FixEhCacheManager.isStop = isStop;
    }

    public synchronized static void put(Object key, Object value) {
        if (!isStop()) {
            Element e = new Element(key, value);
            logger.debug("cache object to fixCache... key:" + key +
                ",size of value" + e.getSerializedSize());
            fixCache.put(e);
        }
    }

    public synchronized static Object get(Object key) {
        if (isStop()) {
            return null;
        }

        Element element = fixCache.get(key);

        if (element != null) {
            logger.debug("get object from fixCache... key:" + key);
        }

        return (element == null) ? null : element.getObjectValue();
    }

    public synchronized static void remove(Object key) {
        logger.debug("remove object from fixCache... key:" + key);
        fixCache.remove(key);
    }

    public synchronized static void removeAll() {
        logger.debug("remove all object from fixCache");
        fixCache.removeAll();
    }

    public synchronized static List getKeys() {
        return fixCache.getKeys();
    }

    public synchronized static int getSize() {
        return fixCache.getSize();
    }

    public synchronized static String getCacheName() {
        return fixCache.getName();
    }

    public synchronized static String getCacheKey(Class _class,
        String methodName, Object... arguments) {
        StringBuffer sb = new StringBuffer("fixCache_");
        sb.append(_class.getName()).append(".").append(methodName);

        if ((arguments != null) && (arguments.length != 0)) {
            for (int i = 0; i < arguments.length; i++) {
                sb.append(".").append(arguments[i]);
            }
        }

        return sb.toString();
    }
}

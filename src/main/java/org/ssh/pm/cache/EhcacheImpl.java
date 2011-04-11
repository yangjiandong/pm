package org.ssh.pm.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.context.ApplicationContext;

/**
 * 本地缓存策略,使用EhCache, 支持限制总数, Idle time/LRU失效,持久化到磁盘等功能.
 *
 * 配置见applicationContext-ehcache.xml与ehcache.xml
 *
 * @author calvin
 */
public class EhcacheImpl {
    private Cache cache;

    public void init(ApplicationContext context) {
        CacheManager ehcacheManager = (CacheManager) context.getBean("ehcacheManager");
        cache = ehcacheManager.getCache("contentInfoCache");
    }

    public Object get(String key) {
        Element element = cache.get(key);
        return element.getObjectValue();
    }

    public void put(String key, Object value) {
        Element element = new Element(key, value);
        cache.put(element);
    }
}

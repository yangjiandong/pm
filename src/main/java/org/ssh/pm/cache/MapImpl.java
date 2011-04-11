package org.ssh.pm.cache;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.MapMaker;

/**
 * 使用ConcurrentHashMap的本地缓存策略.
 *
 * 基于Google Collection实现:
 *
 * 1.加大并发锁数量.
 * 2.每个放入的对象在固定时间后过期.
 * 3.对象均为软引用(内存不足时首先考虑回收的对象).
 *
 * @author calvin
 */
public class MapImpl {

    private ConcurrentMap<String, Object> cache = new MapMaker().concurrencyLevel(32).expiration(86400,
            TimeUnit.SECONDS).softValues().makeMap();

    public Object get(String key) {
        return cache.get(key);
    }

    public void put(String key, Object value) {
        cache.put(key, value);
    }
}

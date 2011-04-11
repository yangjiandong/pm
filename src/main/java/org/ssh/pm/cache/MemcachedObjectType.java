package org.ssh.pm.cache;

/**
 * 统一定义Memcached中存储的各种对象的Key前缀和超时时间.
 *
 * Memcached使用在AccountManager中.
 *
 * @see AccountManager#getLoadedUser(String)
 *
 * @author calvin
 */
public enum MemcachedObjectType {
    //60*60*24*30 (number of seconds in 30 days)
    HZK("hz:", 60 * 60 * 24),
    USER("user:", 60 * 60 * 1);

    private String prefix;
    private int expiredTime;

    MemcachedObjectType(String prefix, int expiredTime) {
        this.prefix = prefix;
        this.expiredTime = expiredTime;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

}

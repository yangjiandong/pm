package org.ssh.pm.jmx.server;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 系统属性配置的MBean实现.
 *
 * @author calvin
 */
public class ServerConfig implements ServerConfigMBean {

    private String nodeName;
    private AtomicBoolean notificationMailEnabled = new AtomicBoolean(true);

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public boolean isNotificationMailEnabled() {
        return notificationMailEnabled.get();
    }

    public void setNotificationMailEnabled(boolean notificationMailEnabled) {
        this.notificationMailEnabled.set(notificationMailEnabled);
    }
}

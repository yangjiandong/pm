package org.ssh.pm.jmx.server;

/**
 * 系统属性配置的MBean接口.
 *
 * @author calvin
 */
public interface ServerConfigMBean {

    /**
     * 服务器节点名.
     */
    public String getNodeName();

    public void setNodeName(String nodeName);

    /**
     * 是否发送通知邮件.
     */
    public boolean isNotificationMailEnabled();

    public void setNotificationMailEnabled(boolean notificationMailEnabled);
}

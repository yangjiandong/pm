package org.ssh.pm.jmx.client;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.utils.web.struts2.Struts2Utils;
import org.ssh.pm.jmx.client.JmxClientService.HibernateStatistics;

//import com.opensymphony.xwork2.ActionSupport;

/**
 * JMX客户端演示的Action.
 *
 * @author ben
 * @author calvin
 */
//@Namespace("/jmx")
@SuppressWarnings("serial")
public class JmxClientAction{
//extends ActionSupport {

    private static Logger logger = LoggerFactory.getLogger(JmxClientAction.class);

    @Autowired
    private JmxClientService jmxClientService;

    //-- 页面属性 --//
    private String nodeName;
    private boolean notificationMailEnabled;
    private HibernateStatistics hibernateStatistics;

    /**
     * 默认函数,显示服务器配置及运行情况.
     */
    //@Override
    public String execute() {
        nodeName = jmxClientService.getNodeName();
        notificationMailEnabled = jmxClientService.isNotificationMailEnabled();
        hibernateStatistics = jmxClientService.getHibernateStatistics();
        //return SUCCESS;
        return "";
    }

    //-- 系统配置 (基于MBean) --//
    /**
     * 修改系统配置的Ajax请求.
     */
    public String saveConfig() {
        try {
            jmxClientService.setNodeName(nodeName);
            jmxClientService.setNotificationMailEnabled(notificationMailEnabled);
            Struts2Utils.renderText("保存配置成功.");
        } catch (Exception e) {
            Struts2Utils.renderText("保存配置失败.");
            logger.error("保存配置失败.", e);
        }
        return null;
    }

    /**
     * 获取最新系统配置, 返回JSON字符串.
     */
    @SuppressWarnings("unchecked")
    public String refreshConfig() {
        Map map = new HashMap();
        try {
            nodeName = jmxClientService.getNodeName();
            notificationMailEnabled = jmxClientService.isNotificationMailEnabled();

            map.put("nodeName", nodeName);
            map.put("notificationMailEnabled", String.valueOf(notificationMailEnabled));
            map.put("message", "获取配置成功.");
        } catch (Exception e) {
            map.put("message", "获取配置失败.");
            logger.error(e.getMessage(), e);
        }

        Struts2Utils.renderJson(map);
        return null;
    }

    //-- Hibernate运行统计(直接读取属性/调用方法) --//
    /**
     * 打印Hibernate统计信息日志.
     */
    public void logSummary() {
        jmxClientService.logSummary();
    }

    //-- 页面属性访问函数 --//
    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public boolean isNotificationMailEnabled() {
        return notificationMailEnabled;
    }

    public void setNotificationMailEnabled(boolean notificationMailEnabled) {
        this.notificationMailEnabled = notificationMailEnabled;
    }

    public HibernateStatistics getHibernateStatistics() {
        return hibernateStatistics;
    }
}

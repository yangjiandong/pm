package org.ssh.pm.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.memcached.SpyMemcachedClient;
import org.springside.modules.security.springsecurity.SpringSecurityUtils;
import org.springside.modules.utils.ServiceException;
import org.springside.modules.utils.encode.JsonBinder;
import org.ssh.pm.cache.MemcachedObjectType;
import org.ssh.pm.common.dao.RoleDao;
import org.ssh.pm.common.dao.UserDao;
import org.ssh.pm.common.dao.UserJdbcDao;
import org.ssh.pm.common.entity.Role;
import org.ssh.pm.common.entity.User;
import org.ssh.pm.common.web.UserSession;
import org.ssh.pm.jms.simple.NotifyMessageProducer;
import org.ssh.pm.jmx.server.ServerConfig;

/**
 * 用户管理类.
 *
 * @author calvin
 */
//Spring Service Bean的标识.
@Component
public class AccountManager {
    private static Logger logger = LoggerFactory.getLogger(AccountManager.class);

    private UserDao userDao;

    private UserJdbcDao userJdbcDao;

    private SpyMemcachedClient spyMemcachedClient;

    private JsonBinder jsonBinder = JsonBinder.buildNonDefaultBinder();

    private ServerConfig serverConfig; //系统配置

    private NotifyMessageProducer notifyProducer; //JMS消息发送

    private PasswordEncoder encoder = new ShaPasswordEncoder();

    private RoleDao roleDao;

    /**
     * 在保存用户时,发送用户修改通知消息, 由消息接收者异步进行较为耗时的通知邮件发送.
     *
     * 如果企图修改超级用户,取出当前操作员用户,打印其信息然后抛出异常.
     *
     */
    //演示指定非默认名称的TransactionManager.
    @Transactional("defaultTransactionManager")
    public void saveUser(User user) {

        if (isSupervisor(user)) {
            logger.warn("操作员{}尝试修改超级管理员用户", SpringSecurityUtils.getCurrentUserName());
            throw new ServiceException("不能修改超级管理员用户");
        }

        String shaPassword = encoder.encodePassword(user.getPassword(), null);
        user.setPassword(shaPassword);

        userDao.save(user);

        sendNotifyMessage(user);
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(User user) {
        //return (user.getId() != null && user.getId().equals("1"));
        return (user.getLoginName() != null && user.getLoginName().equals("admin"));
    }

    @Transactional(readOnly = true)
    public User getUser(String id) {
        return userDao.get(id);
    }

    /**
     * 取得用户, 并对用户的延迟加载关联进行初始化.
     */
    @Transactional(readOnly = true)
    public User getLoadedUser(String id) {
        if (spyMemcachedClient != null) {
            logger.debug("use memecache!!!");
            return getUserFromMemcached(id);
        } else {
            return userJdbcDao.queryObject(id);
        }
    }

    /**
     * 访问Memcached, 使用JSON字符串存放对象以节约空间.
     */
    private User getUserFromMemcached(String id) {

        String key = MemcachedObjectType.USER.getPrefix() + id;

        User user = null;
        String jsonString = spyMemcachedClient.get(key);

        if (jsonString == null) {
            //用户不在 memcached中,从数据库中取出并放入memcached.
            //因为hibernate的proxy问题多多,此处使用jdbc
            user = userJdbcDao.queryObject(id);
            if (user != null) {
                jsonString = jsonBinder.toJson(user);
                spyMemcachedClient.set(key, MemcachedObjectType.USER.getExpiredTime(), jsonString);
            }
        } else {
            user = jsonBinder.fromJson(jsonString, User.class);
        }
        return user;
    }

    /**
     * 按名称查询用户, 并对用户的延迟加载关联进行初始化.
     */
    @Transactional(readOnly = true)
    public User searchLoadedUserByName(String name) {
        User user = userDao.findUniqueBy("name", name);
        userDao.initUser(user);
        return user;
    }

    /**
     * 取得所有用户, 预加载用户的角色.
     */
    @Transactional(readOnly = true)
    public List<User> getAllUserWithRole() {
        List<User> list = userDao.getAllUserWithRoleByDistinctHql();
        logger.info("get {} user sucessful.", list.size());
        return list;
    }

    /**
     * 获取当前用户数量.
     */
    @Transactional(readOnly = true)
    public Long getUserCount() {
        return userDao.getUserCount();
    }

    @Transactional(readOnly = true)
    public User findUserByLoginName(String loginName) {
        return userDao.findUniqueBy("loginName", loginName);
    }

    /**
     * 批量修改用户状态.
     */
    public void disableUsers(List<String> ids) {
        userDao.disableUsers(ids);
    }

    /**
     * 发送用户变更消息.
     *
     * 同时发送只有一个消费者的Queue消息与发布订阅模式有多个消费者的Topic消息.
     */
    private void sendNotifyMessage(User user) {
        if (serverConfig != null && serverConfig.isNotificationMailEnabled() && notifyProducer != null) {
            try {
                notifyProducer.sendQueue(user);
                notifyProducer.sendTopic(user);
            } catch (Exception e) {
                logger.error("消息发送失败", e);
            }
        }
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUserJdbcDao(UserJdbcDao userJdbcDao) {
        this.userJdbcDao = userJdbcDao;
    }

    @Autowired(required = false)
    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Autowired(required = false)
    public void setNotifyProducer(NotifyMessageProducer notifyProducer) {
        this.notifyProducer = notifyProducer;
    }

    @Autowired(required = false)
    public void setSpyMemcachedClient(SpyMemcachedClient spyMemcachedClient) {
        this.spyMemcachedClient = spyMemcachedClient;
    }

    //初始
    @Transactional
    public void initData() {
        if (this.userDao.getUserCount().longValue() != 0) {
            return;
        }

        Role r = new Role();
        r.setName("admin");
        r.setDesc("系统管理员角色");
        this.roleDao.save(r);

        List<Role> rs = new ArrayList<Role>();
        rs.add(r);

        User u = new User();
        //u.setId("1");
        u.setName("管理员");
        u.setLoginName("admin");
        u.setPassword("123");
        u.setEmail("admin@gmail.com");
        u.setCreateBy("初始化");
        u.setStatus("enabled");
        //add role
        u.setRoleList(rs);

        String shaPassword = encoder.encodePassword(u.getPassword(), null);
        u.setPassword(shaPassword);

        userDao.save(u);

        //saveUser(u);
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    /**
     * by sp 取得所有用户.
     */
    @Transactional(readOnly = true)
    public List<User> getAllUserBySp() {
        List<User> list = userJdbcDao.queryBySp("2011.01.01");
        //logger.info("get {} user sucessful.", list.size());
        return list;
    }

    public void createUserInTransaction2(User u) {
        this.userJdbcDao.createUserInTransaction2(u);
    }

    /**
     * 保存用户修改后的密码
     */
    @Transactional
    public Map<String, Object> savePassword(String userName, String newPassword) throws ServiceException {
        Map<String, Object> map = new HashMap<String, Object>();
        String message = "";
        boolean checked = false;
        Session session = this.userDao.getSession();
        try {
            session.beginTransaction();
            User user = userDao.findUniqueBy("name", userName);
            if (user != null) {
                String shaPassword = encoder.encodePassword(newPassword, null);
                user.setPassword(shaPassword);
            }
            userDao.save(user);
            session.getTransaction().commit();
            checked = true;
            message = "OK";
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            checked = false;
            message = "修改密码失败";
        }
        map.put("success", checked);
        map.put("message", message);
        return map;
    }

    @Transactional(readOnly = true)
    public String getNowString() {
        return this.userJdbcDao.getNowString("yyyy.MM.dd HH:mm:ss");
    }

    /**
     * 检查当前用户信息的合法性
     */
    @Transactional(readOnly = true)
    public Map<String, Object> checkUserLegality(HttpServletRequest request)
            throws ServiceException {
        HttpSession session = request.getSession(true);
        session.removeAttribute("userSession");

        Map<String, Object> map = new HashMap<String, Object>();

        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientIp = request.getRemoteAddr();

        boolean checked = true;
        String message = "";

        User user = userDao.findUniqueBy("loginName", userName);

        if (user != null) {
            if (encoder.encodePassword(password, null).equals(user.getPassword())) {
                if (!user.getStatus().equals("disabled")) {

                  UserSession userSession = new UserSession(user);
                  userSession.setClientIp(clientIp);
                  //
                  userSession.setModuleId(1L);
                  session.setAttribute("userSession", userSession);
                } else {
                    checked = false;
                    message = "用户被禁用";
                }
            } else {
                checked = false;
                message = "密码错误";
            }
            userName = user.getName();
            if (userName.equals("")) userName = user.getLoginName();
        } else {
            checked = false;
            message = "用户名错误";
        }
        map.put("success", checked);
        map.put("message", message);
        map.put("userName",userName );
        return map;
    }

    /**
     * 用户注销
     */
    @Transactional(readOnly = true)
    public Map<String, Object> logout(HttpServletRequest request) throws ServiceException {
        Map<String, Object> map = new HashMap<String, Object>();

        String message = "注销失败";
        boolean checked = false;

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            checked = true;
            message = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            checked = false;
            message = "注销时，后台发生异常，注销失败";
        }
        map.put("success", checked);
        map.put("message", message);

        return map;
    }

}

package org.ssh.pm.orm.hibernate;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.SaveOrUpdateEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springside.modules.utils.spring.SpringContextHolder;
import org.ssh.pm.common.service.AccountManager;
import org.ssh.pm.common.web.UserSession;

/**
 * 在自动为entity添加审计信息的Hibernate EventListener.
 *
 * 在hibernate执行saveOrUpdate()时,自动为AuditableEntity的子类添加审计信息.
 *
 * @author calvin
 */
public class AuditListener implements SaveOrUpdateEventListener {

    private static final long serialVersionUID = -7481545873785342485L;
    private static Logger logger = LoggerFactory.getLogger(AuditListener.class);

    public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
        Object object = event.getObject();

        //如果对象是AuditableEntity子类,添加审计信息.
        if (object instanceof AuditableEntity) {
            AuditableEntity entity = (AuditableEntity) object;
            //String loginName = SpringSecurityUtils.getCurrentUserName();
            String loginName = "自定义";

            AccountManager manager = (AccountManager) SpringContextHolder.getBean("accountManager");

            if (entity.getId() == null) {
                //创建新对象
                entity.setCreateTime(manager.getNowString());

                if (entity.getCreateBy() == null) {
                    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                            .getRequestAttributes()).getRequest();
                    UserSession u = (UserSession) request.getSession().getAttribute("userSession");
                    if (u != null) {
                        loginName = u.getAccount().getUserName();
                    }
                    entity.setCreateBy(loginName);
                }
            } else {
                //修改旧对象
                entity.setLastModifyTime(manager.getNowString());

                if (entity.getLastModifyBy() == null) {
                    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                            .getRequestAttributes()).getRequest();
                    UserSession u = (UserSession) request.getSession().getAttribute("userSession");
                    if (u != null) {
                        loginName = u.getAccount().getUserName();
                    }
                    entity.setLastModifyBy(loginName);
                }

                logger.info("{}对象(ID:{}) 被 {} 在 {} 修改", new Object[] { event.getEntityName(), entity.getId(),
                        loginName, new Date() });
            }
        }
    }
}
